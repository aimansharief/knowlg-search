package org.sunbird.mimetype.mgr.impl

import java.io.File

import org.sunbird.common.exception.ClientException
import org.sunbird.graph.dac.model.Node
import org.sunbird.mimetype.mgr.{BaseMimeTypeManager, MimeTypeManager}

import scala.concurrent.{ExecutionContext, Future}


object YouTubeMimeTypeMgrImpl extends BaseMimeTypeManager with MimeTypeManager {

	override def upload(objectId: String, node: Node, uploadFile: File)(implicit ec: ExecutionContext): Future[Map[String, AnyRef]] = {
		throw new ClientException("UPLOAD_DENIED", UPLOAD_DENIED_ERR_MSG)
	}

	override def upload(objectId: String, node: Node, fileUrl: String)(implicit ec: ExecutionContext): Future[Map[String, AnyRef]] = {
		validateUploadRequest(objectId, node, fileUrl)
		Future{Map[String, AnyRef]("identifier" -> objectId, "node_id"->objectId, "content_url"->fileUrl,"artifactUrl" -> fileUrl)}
	}

	override def review(objectId: String, node: Node, isAsync: Boolean)(implicit ec: ExecutionContext): Future[Map[String, AnyRef]] = ???

	override def publish(objectId: String, node: Node, isAsync: Boolean)(implicit ec: ExecutionContext): Future[Map[String, AnyRef]] = ???

}
