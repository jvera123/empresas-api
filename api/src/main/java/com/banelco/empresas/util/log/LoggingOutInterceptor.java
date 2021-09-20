package com.banelco.empresas.util.log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banelco.empresas.util.security.SecurityUtil;

public class LoggingOutInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

	private static final int maxEntitySize = 1024 * 8;
	private static final Logger log = LoggerFactory.getLogger(LoggingOutInterceptor.class);
	private String recInCtx = "empresas.api.rec.in.ctx";

	@Override
	public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
		ApiLogDataOut apiLogDataOut = new ApiLogDataOut();
		apiLogDataOut.setOutDate(new Date());

		Header[] headers = httpRequest.getAllHeaders();
		if (headers != null && headers.length > 0) {
			StringBuilder b = new StringBuilder();
			for (Header header : headers) {
				b.append("[");
				b.append(header.getName());
				b.append(":");
				b.append(header.getValue());
				b.append("]");
				b.append(", ");
			}

			apiLogDataOut.setHeaders(b.substring(0, b.toString().length() - 2));
		}

		apiLogDataOut.setUrl(
				httpContext.getAttribute("http.target_host").toString() + httpRequest.getRequestLine().getUri());
		apiLogDataOut.setMethod(httpRequest.getRequestLine().getMethod());

		if (httpRequest instanceof HttpEntityEnclosingRequest
				&& ((HttpEntityEnclosingRequest) httpRequest).getEntity() != null) {
			HttpEntity rqEntity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			org.apache.commons.io.IOUtils.copy(rqEntity.getContent(), baos);
			byte[] bytes = baos.toByteArray();

			BasicHttpEntity entity = new BasicHttpEntity();
			entity.setContentLength(rqEntity.getContentLength());
			entity.setChunked(rqEntity.isChunked());
			entity.setContentEncoding(rqEntity.getContentEncoding());
			entity.setContentType(rqEntity.getContentType());
			entity.setContent(new ByteArrayInputStream(bytes));

			StringBuilder b = new StringBuilder();
			logInboundEntity(b, new ByteArrayInputStream(bytes));
			apiLogDataOut.setRequest(SecurityUtil.wipeAndProtectData(b.toString()));

			((HttpEntityEnclosingRequest) httpRequest).setEntity(entity);
		}

		httpContext.setAttribute(recInCtx, apiLogDataOut);
	}

	@Override
	public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
		ApiLogDataOut apiLogDataOut = (ApiLogDataOut) httpContext.getAttribute(recInCtx);

		if (apiLogDataOut == null) {
			apiLogDataOut = new ApiLogDataOut();
		}

		apiLogDataOut.setInDate(new Date());
		apiLogDataOut.setStatus(httpResponse.getStatusLine().getStatusCode());

		if (httpResponse.getEntity() != null) {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
					InputStream responseEntityInputStream = httpResponse.getEntity().getContent()) {
				org.apache.commons.io.IOUtils.copy(responseEntityInputStream, baos);
				byte[] bytes = baos.toByteArray();

				BasicHttpEntity entity = new BasicHttpEntity();
				entity.setContentLength(httpResponse.getEntity().getContentLength());
				entity.setChunked(httpResponse.getEntity().isChunked());
				entity.setContentEncoding(httpResponse.getEntity().getContentEncoding());
				entity.setContentType(httpResponse.getEntity().getContentType());
				entity.setContent(new ByteArrayInputStream(bytes));

				apiLogDataOut.setResponse(SecurityUtil
						.wipeAndProtectData(entityToString(new ByteArrayInputStream(bytes), httpResponse.getEntity())));

				httpResponse.setEntity(entity);
			}
		}

		log.info(apiLogDataOut.toString());
		httpContext.removeAttribute(recInCtx);
	}

	private InputStream logInboundEntity(StringBuilder b, InputStream stream) throws IOException {
		if (!stream.markSupported()) {
			stream = new BufferedInputStream(stream);
		}
		stream.mark(maxEntitySize + 1);
		byte[] entity = new byte[maxEntitySize + 1];
		int entitySize = stream.read(entity);
		if (entitySize > 0) {
			b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize)));
			if (entitySize > maxEntitySize) {
				b.append("...more...");
			}
			b.append('\n');
			stream.reset();
			return stream;
		}
		return null;
	}

	private String entityToString(InputStream instream, HttpEntity entity)
			throws IOException, org.apache.http.ParseException {
		try {
			if (entity.getContentLength() > 2147483647L) {
				throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
			} else {
				int i = (int) entity.getContentLength();
				if (i < 0) {
					i = 4096;
				}

				Charset charset = null;

				try {
					ContentType contentType = ContentType.get(entity);
					if (contentType != null) {
						charset = contentType.getCharset();
					}
				} catch (UnsupportedCharsetException var13) {
					throw new UnsupportedEncodingException(var13.getMessage());
				}

				if (charset == null) {
					charset = HTTP.DEF_CONTENT_CHARSET;
				}

				Reader reader = new InputStreamReader(instream, charset);
				CharArrayBuffer buffer = new CharArrayBuffer(i);
				char[] tmp = new char[1024];

				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}

				return buffer.toString();
			}
		} finally {
			instream.close();
		}
	}
}
