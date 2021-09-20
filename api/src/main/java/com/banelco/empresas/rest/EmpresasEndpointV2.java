package com.banelco.empresas.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.banelco.empresas.manager.EmpresaManager;
import com.banelco.empresas.model.dto.ErrorDTO;
import com.banelco.empresas.rest.response.empresas.v2.CategoryResponse;
import com.banelco.empresas.rest.response.empresas.v2.CompanyResponse;
import com.banelco.empresas.service.RubroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Path("/v2/")
@Api(value = "empresas-api-v2")
public class EmpresasEndpointV2 {

	@Autowired
	RubroService rubroService;

	@Autowired
	EmpresaManager empresaManager;

	@GET
	@Path("/{consumer_id}/categories")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get categories", notes = "Query all the categories by consumer_id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categories list", response = CategoryResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response findAllCategoriesByBankId(@Context HttpServletRequest requestContext,
			@ApiParam(name = "consumer_id", value = "Consumer that make the request") @PathParam(value = "consumer_id") String bank)
			throws Exception {
		List<CategoryResponse> response = rubroService.findAllByBankId(bank);
		return Response.status(Response.Status.OK.getStatusCode()).entity(response).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/{consumer_id}/categories/{category_id}/companies/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get companies", notes = "Query companies by consumer_id and category_id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Companies", response = CompanyResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response findCompaniesByBankIdAndCategoryId(@Context HttpServletRequest requestContext,
			@ApiParam(name = "consumer_id", value = "Consumer that make the request") @PathParam(value = "consumer_id") String bank,
			@ApiParam(name = "category_id", value = "Category Id") @PathParam(value = "category_id") String id)
			throws Exception {
		List<CompanyResponse> response = empresaManager.findCompaniesByBankIdAndCategoryId(bank, id);
		return Response.status(Response.Status.OK.getStatusCode()).entity(response).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/{consumer_id}/categories/{category_id}/companies/{company_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get one company", notes = "Query one company by its id and consumer_id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Company", response = CompanyResponse.class),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response findCompanyByBankIdAndCompanyId(@Context HttpServletRequest requestContext,
			@ApiParam(name = "consumer_id", value = "Institution that make the request") @PathParam(value = "consumer_id") String bank,
			@ApiParam(name = "category_id", value = "Category Id") @PathParam(value = "category_id") String categoryId,
			@ApiParam(name = "company_id", value = "Company Id to retrieve") @PathParam(value = "company_id") String companyId)
			throws Exception {
		CompanyResponse response = empresaManager.findCompanyByBankIdAndCompanyId(bank, companyId);
		return Response.status(Response.Status.OK.getStatusCode()).entity(response).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/{consumer_id}/companies")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get companies", notes = "Query all the companies by consumer_id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Companies list", response = CompanyResponse.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error", response = ErrorDTO.class) })
	public Response findAllCompaniesByBankId(@Context HttpServletRequest requestContext,
			@ApiParam(name = "consumer_id", value = "Consumer that make the request") @PathParam(value = "consumer_id") String bank)
			throws Exception {
		List<CompanyResponse> response = empresaManager.findAllCompaniesByBankId(bank);
		return Response.status(Response.Status.OK.getStatusCode()).entity(response).type(MediaType.APPLICATION_JSON)
				.build();
	}
}
