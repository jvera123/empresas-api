package com.banelco.empresas.manager.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.banelco.empresas.exception.CompanyApiException;
import com.banelco.empresas.exception.EmpresasApiException;
import com.banelco.empresas.manager.EmpresaManager;
import com.banelco.empresas.model.dto.ConfigRecargaDTO;
import com.banelco.empresas.model.dto.EmpresaInfoDTO;
import com.banelco.empresas.model.dto.InfoDeudaEnLineaDTO;
import com.banelco.empresas.model.dto.InfoRecargaDTO;
import com.banelco.empresas.model.dto.InfoVidrieraDTO;
import com.banelco.empresas.model.entity.ConfigRecarga;
import com.banelco.empresas.model.entity.Empresa;
import com.banelco.empresas.model.entity.EmpresaConsultaDeudaEnLinea;
import com.banelco.empresas.model.entity.EmpresaDisplayName;
import com.banelco.empresas.model.entity.EmpresaDonacionInfo;
import com.banelco.empresas.model.entity.EmpresaRecargaInfo;
import com.banelco.empresas.model.entity.ImporteDomestico;
import com.banelco.empresas.model.entity.Institucion;
import com.banelco.empresas.model.entity.Prepago;
import com.banelco.empresas.model.entity.Rubro;
import com.banelco.empresas.model.mapper.CompanyMapper;
import com.banelco.empresas.repository.ConfigRecargaRepository;
import com.banelco.empresas.repository.InstitucionRepository;
import com.banelco.empresas.rest.request.Request;
import com.banelco.empresas.rest.response.empresas.EmpresaResponse;
import com.banelco.empresas.rest.response.empresas.PrepagoResponse;
import com.banelco.empresas.rest.response.empresas.RubroResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompanyResponse;
import com.banelco.empresas.service.AyudaEmpresaService;
import com.banelco.empresas.service.DatagridService;
//github.com/PrismaMP-Desarrollo-BE/empresas-api.git
import com.banelco.empresas.service.EmpresaDeudaEnLineaService;
import com.banelco.empresas.service.EmpresaDisplayNameService;
import com.banelco.empresas.service.EmpresaDonacionInfoService;
import com.banelco.empresas.service.EmpresaImporteFijoService;
import com.banelco.empresas.service.EmpresaInfoService;
import com.banelco.empresas.service.EmpresaRecargaInfoService;
import com.banelco.empresas.service.EmpresaService;
import com.banelco.empresas.util.constants.Constantes;
import static com.banelco.empresas.util.constants.Constantes.INSTITUTION_NOT_FOUND_ERROR_MESSAGE;
import static com.banelco.empresas.util.constants.Constantes.INSTITUTION_NOT_FOUND_ERROR_CODE;
import com.banelco.empresas.util.converters.MonedaConverter;
import com.banelco.empresas.util.converters.MontoConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.banelco.empresas.util.constants.Constantes.EFEC;
import static com.banelco.empresas.util.constants.Constantes.DEMO;
import static com.banelco.empresas.util.constants.Constantes.TPGO;

@Service
@PropertySource("classpath:empresas-api.properties")
public class EmpresaManagerImpl implements EmpresaManager {
	Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private AyudaEmpresaService ayudaEmpresaService;

	@Autowired
	private EmpresaInfoService empresaInfoService;

	@Autowired
	private EmpresaDonacionInfoService empresaDonacionInfoService;

	@Autowired
	private EmpresaRecargaInfoService empresaRecargaInfoService;

	@Autowired
	private EmpresaImporteFijoService empresaImporteFijoService;

	@Autowired
	private ConfigRecargaRepository configRecargaRepository;

	@Autowired
	private DatagridService datagridService;

	@Autowired
	private EmpresaDeudaEnLineaService empresaDeudaEnLineaService;
	
	@Autowired
	private InstitucionRepository institucionRepository;
	
	@Autowired
	private EmpresaDisplayNameService empresaDisplayNameService;
	

	@Value("${empresas.nombreArchivoEnDisco}")
	private String nombreArchivoEnDisco;

	@Value("${empresas.longRegArchivoEnDisco}")
	private int longRegArchivoEnDisco;

	@Value("#{'${empresas.datosEBPP}'.split(',')}")
	private List<String> datosEBPP;

	public List<EmpresaResponse> obtenerEmpresas(String institucion) throws EmpresasApiException {
		try {
			return datagridService.getList(institucion);
		} catch (EmpresasApiException e) {
			establecerEmpresas(new Request(institucion, null, null));
			return datagridService.getList(institucion);
		}
	}

	public List<EmpresaResponse> obtenerEmpresasDeUnRubro(String institucion, String rubro) throws Exception {
		List<EmpresaResponse> empresas = this.obtenerEmpresas(institucion);
		List<EmpresaResponse> response = new ArrayList<EmpresaResponse>();
		for (EmpresaResponse empresaResponse : empresas) {
			if (rubro.equals(empresaResponse.getRubro().getIdRubro())) {
				response.add(empresaResponse);
			}
		}
		return response;
	}

	public void establecerEmpresas(Request request) throws EmpresasApiException {
		List<EmpresaResponse> empresas = new ArrayList<>();
		logger.info("Cargar empresas en memoria");
		
		switch (request.getFiid()) {
		case DEMO:
			empresas = convertListToEmpresasResponse(empresaService.obtenerEmpresas(request.getFiid()), null,
					request.getFiid());
			logger.info("Crear archivo EmpresasPMC_YYMMDD");
			try {
				crearArchivoEmpresasPMC(empresas);
				logger.info("Archivo creado exitosamente");
			} catch (IOException e) {
				logger.error("Archivo no creado|" + e.getMessage());
			}
			break;
			
		case TPGO:
			empresas = convertListToEmpresasResponse(empresaService.obtenerEmpresas(request.getFiid()),
					empresaService.obtenerEmpresas(DEMO), request.getFiid());			
			break;
		
		case EFEC:
			empresas = convertListToEmpresasResponse(empresaService.obtenerEmpresas(request.getFiid()), null,
					request.getFiid());
			break;
		
		default:
			logger.info("FIID:"+request.getFiid()+" no se encuentra dentro de los esperados.");
			break;
		}
		datagridService.putList(request.getFiid(), empresas);
		logger.info("Empresas cargadas exitosamente");
	}

	private void crearArchivoEmpresasPMC(List<EmpresaResponse> empresas) throws IOException {
		SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat ddMMyyyy = new SimpleDateFormat("ddMMyyyy");
		File archivo = new File(
				System.getProperty("jboss.server.log.dir") + "/" + nombreArchivoEnDisco + yyMMdd.format(new Date()));
		BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
		bw.write(StringUtils.rightPad("H" + ddMMyyyy.format(new Date()), longRegArchivoEnDisco, " "));
		StringBuilder sb = null;
		for (EmpresaResponse er : empresas) {
			bw.newLine();
			sb = new StringBuilder();
			sb.append("04");
			sb.append(StringUtils.rightPad(er.getRubro().getIdRubro(), 4));
			sb.append(StringUtils.rightPad(er.getCodigo(), 4));
			sb.append(StringUtils.rightPad(er.getNombre(), 25));
			sb.append(StringUtils.rightPad(er.getTituloIdentificacion(), 25));
			sb.append(StringUtils.rightPad(er.getTipoAdhesion().toString(), 1));
			sb.append(StringUtils.rightPad(er.getImportePermitido().toString(), 1));
			sb.append(StringUtils.rightPad(er.getTipoPago().toString(), 1));
			sb.append(StringUtils.leftPad(MonedaConverter.convert(er.getCodigoMoneda()), 3, "0"));
			sb.append(StringUtils.rightPad((er.getDatoAdicional() != null ? er.getDatoAdicional() : " "), 1));
			sb.append(StringUtils.rightPad(er.getTipoEmpresa(), 1));
			sb.append(er.isPermiteUsd() ? "1" : "0");
			sb.append(datosEBPP.contains(er.getCodigo()) ? "S" : " ");
			sb.append(StringUtils.rightPad((er.getCuit() != null ? er.getCuit() : ""), 11));
			if (er.getLongitudClaveMaxima() != null && er.getLongitudClaveMinima() != null
					&& er.getLongitudClaveMaxima().equals(er.getLongitudClaveMinima())) {
				sb.append(StringUtils.leftPad(er.getLongitudClaveMaxima().toString(), 3, "0"));
			} else {
				sb.append("000");
			}
			sb.append(StringUtils.leftPad(
					(er.getLongitudClaveMinima() != null ? er.getLongitudClaveMinima().toString() : "0"), 3, "0"));
			sb.append(StringUtils.leftPad(
					(er.getLongitudClaveMaxima() != null ? er.getLongitudClaveMaxima().toString() : "0"), 3, "0"));
			sb.append(er.isPermitePagosRecurrentes() ? "1" : "0");
			sb.append(er.isSoloConsultas() ? "1" : "0");
			sb.append(StringUtils.rightPad(er.getRubro().getNombre(), 32));
			sb.append(getMarcaPagoTC(er));
			sb.append(StringUtils.rightPad(MontoConverter.convertirMontoMax(er.getMontoMaximoTC()), 5));
			sb.append(StringUtils.rightPad(MontoConverter.convertirMontoMin(er.getMontoMinimoTC()), 3));
			sb.append(StringUtils.rightPad("", 43));
			bw.write(sb.toString());
		}
		bw.newLine();
		bw.write(StringUtils.rightPad("T" + String.format("%06d", empresas.size()), longRegArchivoEnDisco, " "));
		bw.close();
	}

	private static String getMarcaPagoTC(EmpresaResponse e) {
		if (!e.isPermitePagosTCA() && !e.isPermitePagosTCM() && e.isPermitePagosTCV())
			return "1";
		if (!e.isPermitePagosTCA() && e.isPermitePagosTCM() && !e.isPermitePagosTCV())
			return "2";
		if (!e.isPermitePagosTCA() && e.isPermitePagosTCM() && e.isPermitePagosTCV())
			return "3";
		if (e.isPermitePagosTCA() && !e.isPermitePagosTCM() && !e.isPermitePagosTCV())
			return "4";
		if (e.isPermitePagosTCA() && !e.isPermitePagosTCM() && e.isPermitePagosTCV())
			return "5";
		if (e.isPermitePagosTCA() && e.isPermitePagosTCM() && !e.isPermitePagosTCV())
			return "6";
		if (e.isPermitePagosTCA() && e.isPermitePagosTCM() && e.isPermitePagosTCV())
			return "7";
		return " ";
	}

	public List<RubroResponse> convertListToRubroResponse(List<Rubro> rubros) {
		ArrayList<RubroResponse> listRubrosResponse = new ArrayList<RubroResponse>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		for (Rubro rubro : rubros) {
			RubroResponse rubroResponse = new RubroResponse();
			rubroResponse = mapper.convertValue(rubro, RubroResponse.class);
			rubroResponse.setIdRubro(rubro.getIdEmbeddedIdRubro().getIdRubro());
			rubroResponse.setTipo(rubro.getIdEmbeddedIdRubro().getTipo());
			listRubrosResponse.add(rubroResponse);
		}
		Collections.sort(listRubrosResponse, new Comparator<RubroResponse>() {
			public int compare(RubroResponse o1, RubroResponse o2) {
				return o1.getNombre().compareTo(o2.getNombre());
			}
		});
		return listRubrosResponse;
	}

	public List<EmpresaResponse> convertListToEmpresasResponse(List<Empresa> empresas, List<Empresa> empresasSoloPmc,
			String institucion) {
		List<EmpresaResponse> empresasResponse = new ArrayList<EmpresaResponse>();
		ConfigRecargaDTO crDTO = null;
		InfoVidrieraDTO infoVidrieraDTO = null;
		InfoDeudaEnLineaDTO infoDeudaEnLineaDTO = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		for (Empresa empresa : empresas) {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			EmpresaResponse empresaResponse = new EmpresaResponse();
			empresaResponse = mapper.convertValue(empresa, EmpresaResponse.class);
			empresaResponse.setRubro(obtenerRubroResponse(empresa.getRubro()));
			empresaResponse.setCodigo(empresa.getIdEmbeddedIdEmpresa().getCodigo());
			empresaResponse.setFiid("");
			if ((institucion.equals("TPGO") && empresa.getIdEmbeddedIdEmpresa().getFiid().equals("DEMO"))) {
				empresaResponse.setSoloPmc(true);
			} else {
				try {
					EmpresaInfoDTO empresaInfoDTO = empresaInfoService.obtenerInfo(empresaResponse.getCodigo());
					if (empresaInfoDTO != null) {
						empresaResponse.setCantidadVencimientos(empresaInfoDTO.getCantidadVencimientos());
						empresaResponse.setLongitudClaveMaxima(empresaInfoDTO.getLongitudClaveMaxima());
						empresaResponse.setLongitudClaveMinima(empresaInfoDTO.getLongitudClaveMinima());
						empresaResponse.setCuit(empresaInfoDTO.getCuit());
					}
					empresaResponse.setAyuda(ayudaEmpresaService.obtener(empresaResponse.getCodigo()));

					if (Constantes.RUBRO_DONACIONES.equals(empresaResponse.getRubro().getIdRubro())) {
						EmpresaDonacionInfo empresaDonacionInfo = empresaDonacionInfoService
								.obtener(empresaResponse.getCodigo());
						if (empresaDonacionInfo != null && empresaDonacionInfo.getEnVidrieraDonacion()) {
							infoVidrieraDTO = new InfoVidrieraDTO();
							infoVidrieraDTO.setDescripcion(empresaDonacionInfo.getDescripcionVidrieraDonacion());
							infoVidrieraDTO.setOrden(empresaDonacionInfo.getOrdenVidrieraDonacion());
							infoVidrieraDTO.setUrlImagen(empresaDonacionInfo.getUrlVidrieraDonacion());
							infoVidrieraDTO.setUrlPagina(empresaDonacionInfo.getUrlPaginaDonacion());
							infoVidrieraDTO.setUrlImagenDescripcion(
									empresaDonacionInfo.getUrlImagenDescripcionVidrieraDonacion());
							empresaResponse.setInfoVidriera(infoVidrieraDTO);
						}
					} else if (Constantes.RUBRO_RECARGAS.equals(empresaResponse.getRubro().getIdRubro())) {
						ConfigRecarga cr = configRecargaRepository
								.findByIdEmbeddedIdEmpresaFiidAndIdEmbeddedIdEmpresaCodigo(institucion,
										empresaResponse.getCodigo());
						if (cr != null) {
							crDTO = new ConfigRecargaDTO();
							crDTO.setTipo(cr.getTipo());
							String[] importes = cr.getImportes().split("\\|");
							for (String importe : importes) {
								try {
									crDTO.getImportes().add(Double.parseDouble(importe));
								} catch (Exception e) {
								}
							}
							empresaResponse.setConfigRecarga(crDTO);
						}
						EmpresaRecargaInfo empresaRecargaInfo = empresaRecargaInfoService
								.obtener(empresaResponse.getCodigo());
						if (empresaRecargaInfo != null && empresaRecargaInfo.getEnVidrieraRecarga()) {
							InfoRecargaDTO infoRecargaDTO = new InfoRecargaDTO();
							infoRecargaDTO.setUrlImagen(empresaRecargaInfo.getUrlImagen());
							empresaResponse.setInfoRecarga(infoRecargaDTO);
						}
						EmpresaDisplayName empresaDisplayName = empresaDisplayNameService.getEmpresaDisplayName(empresaResponse.getCodigo());
						if (empresaDisplayName != null && empresaDisplayName.getDisplayName() != null) {
							empresaResponse.setNombre(empresaDisplayName.getDisplayName());
						}
					} else if (Arrays.asList(Constantes.RUBROS_DOMESTICAS).contains(
							empresaResponse.getRubro().getIdRubro()) && empresaResponse.getMontoMaximoTC() == -1) {
						try {
							ImporteDomestico importeDomestico = empresaImporteFijoService
									.obtener(empresa.getIdEmbeddedIdEmpresa().getCodigo());
							if (importeDomestico != null) {
								empresaResponse.setMontoMaximoTC(importeDomestico.getImporte());
							} else {
								empresaResponse
										.setMontoMaximoTC(Double.parseDouble(empresaResponse.getCodigo().substring(1)));
							}
						} catch (Exception e) {
						}
					}
					if (Arrays.asList(Constantes.EMPRESAS_IMPORTE_FIJO)
							.contains(empresa.getIdEmbeddedIdEmpresa().getCodigo())) {
						try {
							ImporteDomestico importeDomestico = empresaImporteFijoService
									.obtener(empresa.getIdEmbeddedIdEmpresa().getCodigo());
							if (importeDomestico != null) {
								empresaResponse.setMontoMaximoTC(importeDomestico.getImporte());
							}
						} catch (Exception e) {
						}
					}
					EmpresaConsultaDeudaEnLinea empresaConsultaDeudaEnLinea = empresaDeudaEnLineaService.obtener(empresaResponse.getCodigo());
					if (empresaConsultaDeudaEnLinea != null) {
						infoDeudaEnLineaDTO = new InfoDeudaEnLineaDTO();
						infoDeudaEnLineaDTO.setCodigoEmpresa(empresaConsultaDeudaEnLinea.getCodigoEmpresa());
						infoDeudaEnLineaDTO.setDescripcionAdicional(empresaConsultaDeudaEnLinea.getDescripcionAdicional());
						empresaResponse.setInfoDeudaEnLinea(infoDeudaEnLineaDTO);
					}
				} catch (EmpresasApiException e) {
				}
			}
			if (!empresasResponse.contains(empresaResponse)) {
				empresasResponse.add(empresaResponse);
				datagridService.putCompany(institucion + "-" + empresaResponse.getCodigo(), empresaResponse);
			}
			Collections.sort(empresasResponse, new Comparator<EmpresaResponse>() {
				public int compare(EmpresaResponse o1, EmpresaResponse o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
			stopWatch.stop();
			logger.debug("Empresa cargada: " + empresaResponse.getCodigo() + " en " + stopWatch.getTotalTimeMillis()
					+ " ms");
		}
		if (empresasSoloPmc != null) {
			for (Empresa empresa : empresasSoloPmc) {
				StopWatch stopWatch = new StopWatch();
				stopWatch.start();
				EmpresaResponse empresaResponse = new EmpresaResponse();
				empresaResponse = mapper.convertValue(empresa, EmpresaResponse.class);
				empresaResponse.setRubro(obtenerRubroResponse(empresa.getRubro()));
				empresaResponse.setCodigo(empresa.getIdEmbeddedIdEmpresa().getCodigo());
				empresaResponse.setFiid("");
				empresaResponse.setSoloPmc(true);
				if (!empresasResponse.contains(empresaResponse)) {
					empresasResponse.add(empresaResponse);
					datagridService.putCompany(institucion + "-" + empresaResponse.getCodigo(), empresaResponse);
				}
				Collections.sort(empresasResponse, new Comparator<EmpresaResponse>() {
					public int compare(EmpresaResponse o1, EmpresaResponse o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
				stopWatch.stop();
				logger.debug("Empresa cargada: " + empresaResponse.getCodigo() + " en " + stopWatch.getTotalTimeMillis()
						+ " ms");
			}
		}
		empresaInfoService.evict();
		ayudaEmpresaService.evict();
		empresaDonacionInfoService.evict();
		empresaRecargaInfoService.evict();
		empresaImporteFijoService.evict();
		empresaDeudaEnLineaService.evict();
		return empresasResponse;
	}

	public List<PrepagoResponse> convertListToPrepagosResponse(List<Prepago> prepagos) {
		List<PrepagoResponse> prepagosResponse = new ArrayList<PrepagoResponse>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		for (Prepago prepago : prepagos) {
			PrepagoResponse prepagoResponse = new PrepagoResponse();
			prepagoResponse = mapper.convertValue(prepago, PrepagoResponse.class);
			prepagoResponse.setRubro(obtenerRubroResponse(prepago.getRubro()));
			prepagosResponse.add(prepagoResponse);
		}
		return prepagosResponse;
	}

	public RubroResponse obtenerRubroResponse(Rubro rubroPrincipal) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		RubroResponse rubroResponse = new RubroResponse();
		rubroResponse = mapper.convertValue(rubroPrincipal, RubroResponse.class);
		rubroResponse.setIdRubro(rubroPrincipal.getIdEmbeddedIdRubro().getIdRubro());
		rubroResponse.setTipo(rubroPrincipal.getIdEmbeddedIdRubro().getTipo());
		return rubroResponse;
	}

	public EmpresaResponse obtenerEmpresaPorInstitucionYCodigo(String institucion, String codigo) throws Exception {
		return datagridService.getCompany(institucion + "-" + codigo);
	}

	public CompanyResponse findCompanyByBankIdAndCompanyId(String institution, String id) throws Exception {
		Institucion institucionObj = institucionRepository.findByConsumerId(institution);
		verficarInstitucion(institucionObj);
		
		EmpresaResponse empresaResponse = null;
		try {
			empresaResponse = this.obtenerEmpresaPorInstitucionYCodigo(institucionObj.getRefreshFiid(), id);
		} catch (EmpresasApiException e) {
			throw new CompanyApiException(e); 
		}
		
		return CompanyMapper.map(empresaResponse);
	}

	public List<CompanyResponse> findCompaniesByBankIdAndCategoryId(String institution, String categoryId)
			throws Exception {
		Institucion institucionObj = institucionRepository.findByConsumerId(institution);
		verficarInstitucion(institucionObj);
		
		List<EmpresaResponse> empresasResponse = this.obtenerEmpresasDeUnRubro(institucionObj.getRefreshFiid(), categoryId);
		return CompanyMapper.map(empresasResponse);
	}

	@Override
	public List<CompanyResponse> findAllCompaniesByBankId(String institution) throws EmpresasApiException {
		Institucion institucionObj = institucionRepository.findByConsumerId(institution);
		verficarInstitucion(institucionObj);
		
		List<EmpresaResponse> empresasResponse = this.obtenerEmpresas(institucionObj.getRefreshFiid());
		return CompanyMapper.map(empresasResponse);
	}
	
	private void verficarInstitucion(Institucion institucionObj) throws EmpresasApiException {
		if(institucionObj == null) {
			throw new EmpresasApiException(INSTITUTION_NOT_FOUND_ERROR_MESSAGE, INSTITUTION_NOT_FOUND_ERROR_CODE);
		}
	}
	
}
