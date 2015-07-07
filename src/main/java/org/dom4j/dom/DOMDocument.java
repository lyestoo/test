/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: DOMDocument.java,v 1.6 2001/06/20 18:59:23 jstrachan Exp $
 */

package org.dom4j.dom;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.tree.DefaultDocument;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/** <p><code>DOMDocument</code> implements an XML document which 
  * supports the W3C DOM API.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 1.6 $
  */
public class DOMDocument extends DefaultDocument implements org.w3c.dom.Document {

    /** The <code>DocumentFactory</code> instance used by default */
    private static final DocumentFactory DOCUMENT_FACTORY = DOMDocumentFactory.getInstance();
    

    public DOMDocument() { 
    }

    public DOMDocument(String name) { 
        super(name);
    }

    public DOMDocument(Element rootElement) { 
        super(rootElement);
    }

    public DOMDocument(DocumentType docType) { 
        super(docType);
    }

    public DOMDocument(Element rootElement, DocumentType docType) { 
        super(rootElement, docType);
    }

    public DOMDocument(String name, Element rootElement, DocumentType docType) { 
        super(name, rootElement, docType);
    }

    
    // org.w3c.dom.Node interface
    //-------------------------------------------------------------------------        
    public String getNamespaceURI() {
        return DOMNodeHelper.getNamespaceURI(this);
    }

    public String getPrefix() {
        return DOMNodeHelper.getPrefix(this);
    }
    
    public void setPrefix(String prefix) throws DOMException {
        DOMNodeHelper.setPrefix(this, prefix);
    }

    public String getLocalName() {
        return DOMNodeHelper.getLocalName(this);
    }

    public String getNodeName() {
        return getName();
    }
    
    //already part of API  
    //
    //public short getNodeType();
    

    
    public String getNodeValue() throws DOMException {
        return DOMNodeHelper.getNodeValue(this);
    }
    
    public void setNodeValue(String nodeValue) throws DOMException {
        DOMNodeHelper.setNodeValue(this, nodeValue);
    }
        

    public org.w3c.dom.Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }
    
    public NodeList getChildNodes() {
        return DOMNodeHelper.createNodeList( content() );
    }

    public org.w3c.dom.Node getFirstChild() {
        return DOMNodeHelper.asDOMNode( node(0) );
    }

    public org.w3c.dom.Node getLastChild() {
        return DOMNodeHelper.asDOMNode( node( nodeCount() - 1 ) );
    }

    public org.w3c.dom.Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public org.w3c.dom.Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

    public NamedNodeMap getAttributes() {
        return DOMNodeHelper.getAttributes(this);
    }
    
    public org.w3c.dom.Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public org.w3c.dom.Node insertBefore(
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node refChild
    ) throws DOMException {
        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public org.w3c.dom.Node replaceChild(
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node oldChild
    ) throws DOMException {
        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild) throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild) throws DOMException {
        return DOMNodeHelper.appendChild(this, newChild);
    }

    public boolean hasChildNodes() {
        return nodeCount() > 0;
    }

    public org.w3c.dom.Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }
    
    
    // org.w3c.dom.Document interface
    //-------------------------------------------------------------------------            
    public NodeList getElementsByTagName(String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagName( list, this, name );
        return DOMNodeHelper.createNodeList( list );
    }
    
    public NodeList getElementsByTagNameNS(
        String namespaceURI, String localName
    ) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagNameNS(list, this, namespaceURI, localName );
        return DOMNodeHelper.createNodeList( list );
    }

    
    public org.w3c.dom.DocumentType getDoctype() {
        return DOMNodeHelper.asDOMDocumentType( getDocType() );
    }

    public org.w3c.dom.DOMImplementation getImplementation() {
        return DOMDocumentFactory.singleton;
    }

    public org.w3c.dom.Element getDocumentElement() {
        return DOMNodeHelper.asDOMElement( getRootElement() );
    }

    public org.w3c.dom.Element createElement(String tagName) throws DOMException {
        return new DOMElement(tagName);
    }

    public org.w3c.dom.DocumentFragment createDocumentFragment() {
        DOMNodeHelper.notSupported();
        return null;
    }

    public org.w3c.dom.Text createTextNode(String data) {
        return new DOMText(data);
    }

    public org.w3c.dom.Comment createComment(String data) {
        return new DOMComment(data);
    }

    public org.w3c.dom.CDATASection createCDATASection(String data) throws DOMException {
        return new DOMCDATA(data);
    }

    public org.w3c.dom.ProcessingInstruction createProcessingInstruction(
        String target, String data
    ) throws DOMException {
        return new DOMProcessingInstruction(target, data);
    }

    public org.w3c.dom.Attr createAttribute(String name) throws DOMException {
        return new DOMAttribute( DOCUMENT_FACTORY.createQName(name) );
    }
    
    public org.w3c.dom.EntityReference createEntityReference(String name) throws DOMException {
        return new DOMEntityReference(name);
    }

    public org.w3c.dom.Node importNode(
        org.w3c.dom.Node importedNode, boolean deep
    ) throws DOMException {
        DOMNodeHelper.notSupported();
        return null;
    }

    public org.w3c.dom.Element createElementNS(
        String namespaceURI, String qualifiedName
    ) throws DOMException {
        QName qname = DOCUMENT_FACTORY.createQName( qualifiedName, namespaceURI );
        return new DOMElement( qname );
    }

    public org.w3c.dom.Attr createAttributeNS(
        String namespaceURI, String qualifiedName
    ) throws DOMException {
        QName qname = DOCUMENT_FACTORY.createQName( qualifiedName, namespaceURI );
        return new DOMAttribute( qname );
    }


    public org.w3c.dom.Element getElementById(String elementId) {
        return DOMNodeHelper.asDOMElement( elementByID( elementId ) );
    }
    
    
    
    // Implementation methods
    //-------------------------------------------------------------------------            
    protected DocumentFactory getDocumentFactory() {
        return DOCUMENT_FACTORY;
    }
    
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id: DOMDocument.java,v 1.6 2001/06/20 18:59:23 jstrachan Exp $
 */