package com.suidifu.jpmorgan.entity.unionpay;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class TransactionResultXmlModel {
	
	@XStreamAlias("QUERY_SN")
	private String querySn; // 要查询的交易流水
	
	@XStreamAlias("QUERY_REMARK")
	private String quertRemark; //查询备注
	
	@XStreamImplicit(itemFieldName = "RET_DETAIL")
	private List<TransactionResultDetailNode> detailNodes;
	
	private static XStream xStream;
	
	static{
		xStream = new XStream();
		xStream.autodetectAnnotations(true);
		xStream.ignoreUnknownElements();

		xStream.alias("QUERY_TRANS", TransactionResultXmlModel.class);
		xStream.alias("RET_DETAILS", TransactionResultXmlModel.class);
		xStream.alias("INFO", UnionPayRtnInfo.class);
	}

	public String getQuerySn() {
		return querySn;
	}

	public void setQuerySn(String querySn) {
		this.querySn = querySn;
	}

	public String getQuertRemark() {
		return quertRemark;
	}

	public void setQuertRemark(String quertRemark) {
		this.quertRemark = quertRemark;
	}

	public List<TransactionResultDetailNode> getDetailNodes() {
		return detailNodes;
	}

	public void setDetailNodes(List<TransactionResultDetailNode> detailNodes) {
		this.detailNodes = detailNodes;
	}

	public static XStream getxStream() {
		return xStream;
	}

	public static void setxStream(XStream xStream) {
		TransactionResultXmlModel.xStream = xStream;
	}
	
	public static TransactionResultXmlModel initialization(UnionPayResult result) {//TODO test
		TransactionResultXmlModel queryResult = new TransactionResultXmlModel();
		
		String rspPacket = result.getResponsePacket();
		try {
			Document document = DocumentHelper.parseText(rspPacket);
			Node queryTransNode = document.selectSingleNode("GZELINK/BODY/QUERY_TRANS");
			Node retDetailNodel = document.selectSingleNode("GZELINK/BODY/RET_DETAILS");
			
			if(queryTransNode != null && retDetailNodel != null) {
				String queryTransXml = queryTransNode.asXML();
				String retDetailsXml = retDetailNodel.asXML();

				queryResult = (TransactionResultXmlModel) xStream.fromXML(queryTransXml, queryResult);
				queryResult = (TransactionResultXmlModel) xStream.fromXML(retDetailsXml, queryResult);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return queryResult;
	}
	
	
	public static UnionPayRtnInfo parseInfo(UnionPayResult result){
		String rspPacket = result.getResponsePacket();
		try {
			Document document = DocumentHelper.parseText(rspPacket);
			Node infoNode = document.selectSingleNode("GZELINK/INFO");
			
			if(infoNode != null ) {
				String infoXml = infoNode.asXML();

				UnionPayRtnInfo unionPayRtnInfo = (UnionPayRtnInfo)xStream.fromXML(infoXml);
				return unionPayRtnInfo;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
