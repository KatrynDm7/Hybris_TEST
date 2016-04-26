/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.core.jco.monitor.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sap.conn.jco.monitor.JCoConnectionData;

import de.hybris.platform.sap.core.jco.constants.SapcorejcoConstants;
import de.hybris.platform.sap.core.jco.monitor.JCoMonitorException;


/**
 * JCo connection monitor manager.
 */
public class JCoMonitorJAXBHandler
{

	private static final Logger LOG = Logger.getLogger(JCoMonitorJAXBHandler.class.getName());

	/**
	 * Package which contains the JAXB items for JCo connection monitoring.
	 */
	public static final String JAXB_CLUSTER_PACKAGE = "de.hybris.platform.sap.core.jco.monitor.jaxb";

	/**
	 * Generates one single cluster snapshot for all passed node snapshots into the output stream.
	 * 
	 * @param os
	 *           output stream
	 * @param nodes
	 *           JAXB content
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public void generateSnapshot(final OutputStream os, final Nodes nodes) throws JCoMonitorException // NOPMD
	{
		JAXBContext jaxbContext;
		Marshaller marshaller;
		try
		{
			jaxbContext = JAXBContext.newInstance(JAXB_CLUSTER_PACKAGE, this.getClass().getClassLoader());
			marshaller = jaxbContext.createMarshaller();
		}
		catch (final JAXBException e)
		{
			throw new JCoMonitorException("Exception when creating the JAXB content or marshaller for package "
					+ JAXB_CLUSTER_PACKAGE, e);
		}

		try
		{
			marshaller.marshal(nodes, os);
		}
		catch (final JAXBException e)
		{
			throw new JCoMonitorException("Exception during marhalling JAXB content for package " + JAXB_CLUSTER_PACKAGE, e);
		}
	}

	/**
	 * Generates one single cluster snapshot for all passed node snapshots into the output stream.
	 * 
	 * @param os
	 *           output stream
	 * @param snapshots
	 *           node snapshot map
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public void generateSnapshot(final OutputStream os, final Map<Integer, String> snapshots) throws JCoMonitorException
	{
		final Nodes nodes = generateNodes(snapshots);
		generateSnapshot(os, nodes);
	}

	/**
	 * Generates one single cluster snapshot for all passed node snapshots into the output stream.
	 * 
	 * @param nodes
	 *           JAXB content
	 * @return snapshot as string
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public String generateSnapshot(final Nodes nodes) throws JCoMonitorException
	{
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		generateSnapshot(os, nodes);
		return os.toString();
	}

	/**
	 * Generates JAXB nodes from the passed cluster snapshot XML string.
	 * 
	 * @param snapshot
	 *           snapshot XML string
	 * @return JAXB content
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public Nodes generateNodes(final String snapshot) throws JCoMonitorException
	{
		JAXBContext jaxbContext;
		Unmarshaller unmarshaller;		
		try
		{
			jaxbContext = JAXBContext.newInstance(JAXB_CLUSTER_PACKAGE, this.getClass().getClassLoader());
			unmarshaller = jaxbContext.createUnmarshaller();
		}
		catch (final JAXBException e)
		{
			throw new JCoMonitorException("Exception when creating the JAXB content or marshaller for package "
					+ JAXB_CLUSTER_PACKAGE, e);
		}

		// Unmarshall it
		final StringReader stringReader = new StringReader(snapshot);
		Nodes nodes;
		try
		{

			DocumentBuilder documentBuilder = JcoDocumentBuilderFactory.getInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(new InputSource(stringReader));
			
			nodes = (Nodes) unmarshaller.unmarshal(document);
		}
		catch (final  JAXBException | SAXException | IOException | ParserConfigurationException e)
		{
			throw new JCoMonitorException("Exception during unmarhalling single node snapshot for package " + JAXB_CLUSTER_PACKAGE,
					e);
		}
		finally
		{			
			if(stringReader != null){
				safeClose(stringReader);
			}
		}
		return nodes;
	}

	/**
	 * Generates JAXB nodes from multiple passed node snapshot XML strings.
	 * 
	 * @param snapshots
	 *           node snapshot map
	 * @return JAXB content
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public Nodes generateNodes(final Map<Integer, String> snapshots) throws JCoMonitorException
	{
		final ObjectFactory objectFactory = new ObjectFactory();
		final Nodes nodes = objectFactory.createNodes();
		nodes.setSingleNodeResult(false);
		final List<NodeType> nodeList = nodes.getNode();
		for (final Entry<Integer, String> nodeSnapshotEntry : snapshots.entrySet())
		{
			NodeType localNode;
			if (nodeSnapshotEntry.getValue() == null)
			{
				localNode = objectFactory.createNodeType();
				localNode.setId(nodeSnapshotEntry.getKey());
				localNode.setResultAvailable(false);
			}
			else
			{
				// Get snapshot as JAXB content
				final Nodes localNodes = generateNodes(nodeSnapshotEntry.getValue());
				localNode = localNodes.getNode().get(0);
			}
			nodeList.add(localNode);
		}
		return nodes;
	}

	/**
	 * Generates JAXB nodes from multiple passed node snapshot XML strings.
	 * 
	 * @param file
	 *           file
	 * 
	 * @return JAXB content
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public Nodes generateNodes(final File file) throws JCoMonitorException
	{
		JAXBContext jaxbContext;		
		Nodes nodes;
		Reader reader= null;
		InputStream fileInputStream = null;
		try
		{						
		
			jaxbContext = JAXBContext.newInstance(JAXB_CLUSTER_PACKAGE);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			DocumentBuilder documentBuilder = JcoDocumentBuilderFactory.getInstance().newDocumentBuilder();
			fileInputStream = new FileInputStream(file);
			reader = new InputStreamReader(fileInputStream,"UTF-8");	
			Document document = documentBuilder.parse(new InputSource(reader));
			
			nodes = (Nodes) unmarshaller.unmarshal(document);
		}
		catch (final JAXBException | ParserConfigurationException | SAXException | IOException   e)
		{
			throw new JCoMonitorException("Error reading unmarshalling file " + file.getAbsolutePath() + ": " + e.getMessage(), e);
		} 
		finally
		{	
			if(fileInputStream != null){
				safeClose(fileInputStream);
			}
			
			if(reader != null){
				safeClose(reader);
			}
		}
		
		return nodes;
	}

	/**
	 * Generates JAXB nodes from multiple passed node snapshot XML strings.
	 * 
	 * @param is
	 *           input stream
	 * 
	 * @return JAXB content
	 * @throws JCoMonitorException
	 *            {@link JCoMonitorException}
	 */
	public Nodes generateNodes(final InputStream is) throws JCoMonitorException
	{
		JAXBContext jaxbContext;
		Nodes nodes;
		try
		{
			jaxbContext = JAXBContext.newInstance(JAXB_CLUSTER_PACKAGE);
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				
			DocumentBuilder documentBuilder = JcoDocumentBuilderFactory.getInstance().newDocumentBuilder();
			Document document = documentBuilder.parse(new InputSource(is));	
			
			nodes = (Nodes) unmarshaller.unmarshal(document);
		}
		catch (final JAXBException | SAXException | ParserConfigurationException | IOException  e)
		{
			throw new JCoMonitorException("Error reading unmarshalling inputstream " + is + ": " + e.getMessage(), e);
		}finally{
		
			if(is != null){
				safeClose(is);
			}
		}
		return nodes;
	}

	/**
	 * Generates the JAXB nodes from JCoConnectionData for one node id.
	 * 
	 * @param nodeId
	 *           node id
	 * @param connectionDataList
	 *           list of JCo connection data
	 * @return node (root) node
	 */
	public Nodes generateNodesFromJCoConnectionData(final int nodeId, final List<? extends JCoConnectionData> connectionDataList)
	{
		final ObjectFactory objectFactory = new ObjectFactory();

		final Nodes nodes = objectFactory.createNodes();
		nodes.setSingleNodeResult(true);
		final List<NodeType> nodeList = nodes.getNode();

		final NodeType node = objectFactory.createNodeType();
		node.setId(nodeId);
		node.setResultAvailable(true);
		nodeList.add(node);

		final JcoConnectionsType jcoConnections = objectFactory.createJcoConnectionsType();
		node.setJcoConnections(jcoConnections);
		final List<JcoConnectionType> jcoConnectionList = jcoConnections.getJcoConnection();

		for (final JCoConnectionData jCoConnectionData : connectionDataList)
		{
			final JcoConnectionType jcoConnection = objectFactory.createJcoConnectionType();
			jcoConnection.setAbapClient(jCoConnectionData.getAbapClient());
			jcoConnection.setAbapHost(jCoConnectionData.getAbapHost());
			jcoConnection.setAbapLanguage(jCoConnectionData.getAbapLanguage());
			jcoConnection.setAbapSystemNumber(jCoConnectionData.getAbapSystemNumber());
			jcoConnection.setAbapUser(jCoConnectionData.getAbapUser());
			jcoConnection.setApplicationName(jCoConnectionData.getApplicationName());
			jcoConnection.setConnectionHandle(jCoConnectionData.getConnectionHandle());
			jcoConnection.setConnectionType(jCoConnectionData.getConnectionType());
			jcoConnection.setConversationId(jCoConnectionData.getConvId());
			jcoConnection.setDsrPassport(jCoConnectionData.getDSRPassportAsString());
			jcoConnection.setFunctionModuleName(jCoConnectionData.getFunctionModuleName());
			jcoConnection.setGroupName(jCoConnectionData.getGroupName());
			jcoConnection.setLastActivityTimestamp(jCoConnectionData.getLastActivityTimestamp());
			jcoConnection
					.setLastActivityTimestampString(jCoConnectionData.getLastActivityTimestampAsString(new GregorianCalendar()));
			jcoConnection.setProtocol(jCoConnectionData.getProtocol());
			jcoConnection.setRfcDestinationName(getRFCDestinationNameFromGroupName(jCoConnectionData.getGroupName()));
			jcoConnection.setState(jCoConnectionData.getState());
			jcoConnection.setStateString(jCoConnectionData.getStateAsString());
			jcoConnection.setSystemId(jCoConnectionData.getSystemID());
			jcoConnection.setThreadId(jCoConnectionData.getThreadId());
			jcoConnection.setThreadName(jCoConnectionData.getThreadName());
			jcoConnectionList.add(jcoConnection);
		}

		return nodes;

	}

	/**
	 * Extracts the RFC destination name from the group name.
	 * 
	 * @param groupName
	 *           group name of JCo connection
	 * @return RFC destination name
	 */
	public String getRFCDestinationNameFromGroupName(final String groupName)
	{
		final String[] groupNameSplitted = groupName.split("\\|");
		return groupNameSplitted[0].replace(SapcorejcoConstants.RFCDESTINATION_NAME_PREFIX, "");
	}
	
	protected void safeClose(final Reader resource)
	{
		if (resource != null)
		{
			try{				
				resource.close();				
			}
			catch(IOException ex){				
				LOG.error("Error when closing the input stream reader! " + ex.getMessage(), ex);			
			}
		}
	}
	
	protected void safeClose(final InputStream resource)
	{
		if (resource != null)
		{
			try{
				resource.close();				
			}
			catch(IOException ex){				
				LOG.error("Error when closing the input stream! " + ex.getMessage(), ex);			
			}
		}
	}

}
