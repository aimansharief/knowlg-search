package org.sunbird.content.actors

import java.util

import javax.inject.Inject
import org.apache.commons.lang3.StringUtils
import org.sunbird.actor.core.BaseActor
import org.sunbird.cache.impl.RedisCache
import org.sunbird.common.ContentParams
import org.sunbird.common.dto.{Request, Response, ResponseHandler}
import org.sunbird.common.exception.{ClientException}

import org.sunbird.util.RequestUtil

import org.sunbird.content.upload.mgr.UploadManager

import org.sunbird.graph.OntologyEngineContext
import org.sunbird.graph.nodes.DataNode
import org.sunbird.graph.utils.NodeUtil

import scala.collection.JavaConverters
import scala.concurrent.{ExecutionContext, Future}

class ContentActor @Inject() (implicit oec: OntologyEngineContext) extends BaseActor {

	implicit val ec: ExecutionContext = getContext().dispatcher

	override def onReceive(request: Request): Future[Response] = {
		request.getOperation match {
			case "createContent" => create(request)
			case "readContent" => read(request)
			case "updateContent" => update(request)
			case "uploadContent" => upload(request)
			case _ => ERROR(request.getOperation)
		}
	}

	def create(request: Request): Future[Response] = {
		populateDefaultersForCreation(request)
		RequestUtil.restrictProperties(request)
		if (StringUtils.equalsIgnoreCase(request.get("contentType").asInstanceOf[String], "eTextBook"))
			request.getContext.put("schemaName", "etextbook")
		DataNode.create(request).map(node => {
			val response = ResponseHandler.OK
			response.put("identifier", node.getIdentifier)
			response.put("node_id", node.getIdentifier)
			response.put("versionKey", node.getMetadata.get("versionKey"))
			response
		})
	}

	def read(request: Request): Future[Response] = {
		val fields: util.List[String] = JavaConverters.seqAsJavaListConverter(request.get("fields").asInstanceOf[String].split(",").filter(field => StringUtils.isNotBlank(field) && !StringUtils.equalsIgnoreCase(field, "null"))).asJava
		request.getRequest.put("fields", fields)
		DataNode.read(request).map(node => {
			val metadata: util.Map[String, AnyRef] = NodeUtil.serialize(node, fields, request.getContext.get("schemaName").asInstanceOf[String], request.getContext.get("version").asInstanceOf[String])
			metadata.put("identifier", node.getIdentifier.replace(".img", ""))
			val response: Response = ResponseHandler.OK
			response.put("content", metadata)
			response
		})
	}

	def update(request: Request): Future[Response] = {
		populateDefaultersForUpdation(request)
		if (StringUtils.isBlank(request.getRequest.getOrDefault("versionKey", "").asInstanceOf[String])) throw new ClientException("ERR_INVALID_REQUEST", "Please Provide Version Key!")
		RequestUtil.restrictProperties(request)
		if (StringUtils.equalsIgnoreCase(request.get("contentType").asInstanceOf[String], "eTextBook"))
			request.getContext.put("schemaName", "etextbook")
		DataNode.update(request).map(node => {
			val response: Response = ResponseHandler.OK
			val identifier: String = node.getIdentifier.replace(".img", "")
			response.put("node_id", identifier)
			response.put("identifier", identifier)
			response.put("versionKey", node.getMetadata.get("versionKey"))
			response
		})
	}

	def upload(request: Request): Future[Response] = {
		val identifier: String = request.getContext.getOrDefault("identifier", "").asInstanceOf[String]
		val readReq = new Request(request)
		readReq.put("identifier", identifier)
		readReq.put("fields", new util.ArrayList[String])
		DataNode.read(readReq).map(node => {
			UploadManager.upload(request, node)
		}).flatMap(f => f)
	}

	def populateDefaultersForCreation(request: Request) = {
		setDefaultsBasedOnMimeType(request, ContentParams.create.name)
		setDefaultLicense(request)
	}

	private def setDefaultLicense(request: Request): Unit = {
		if (StringUtils.isEmpty(request.getRequest.getOrDefault("license", "").asInstanceOf[String])) {
			val cacheKey = "channel_" + request.getRequest.getOrDefault("channel", "").asInstanceOf[String] + "_license"
			val defaultLicense = RedisCache.get(cacheKey, null, 0)
			if (StringUtils.isNotEmpty(defaultLicense)) request.getRequest.put("license", defaultLicense)
			else System.out.println("Default License is not available for channel: " + request.getRequest.getOrDefault("channel", "").asInstanceOf[String])
		}
	}

	def populateDefaultersForUpdation(request: Request) = {
		if (request.getRequest.containsKey(ContentParams.body.name)) request.put(ContentParams.artifactUrl.name, null)
	}

	private def setDefaultsBasedOnMimeType(request: Request, operation: String): Unit = {
		val mimeType = request.get(ContentParams.mimeType.name).asInstanceOf[String]
		if (StringUtils.isNotBlank(mimeType) && operation.equalsIgnoreCase(ContentParams.create.name)) {
			if (StringUtils.equalsIgnoreCase("application/vnd.ekstep.plugin-archive", mimeType)) {
				val code = request.get(ContentParams.code.name).asInstanceOf[String]
				if (null == code || StringUtils.isBlank(code)) throw new ClientException("ERR_PLUGIN_CODE_REQUIRED", "Unique code is mandatory for plugins")
				request.put(ContentParams.identifier.name, request.get(ContentParams.code.name))
			}
			else request.put(ContentParams.osId.name, "org.ekstep.quiz.app")
			if (mimeType.endsWith("archive") || mimeType.endsWith("vnd.ekstep.content-collection") || mimeType.endsWith("epub")) request.put(ContentParams.contentEncoding.name, ContentParams.gzip.name)
			else request.put(ContentParams.contentEncoding.name, ContentParams.identity.name)
			if (mimeType.endsWith("youtube") || mimeType.endsWith("x-url")) request.put(ContentParams.contentDisposition.name, ContentParams.online.name)
			else request.put(ContentParams.contentDisposition.name, ContentParams.inline.name)
		}
	}
}
