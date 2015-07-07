/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: Node.java,v 1.22 2001/09/15 14:55:14 jstrachan Exp $
 */

package org.dom4j;

import java.io.IOException;
import java.io.Writer;

import java.util.List;

/** <p><code>Node</code> defines the polymorphic behavior 
  * for all XML nodes in a DOM4J tree.</p>
  *
  * <p>A node may optionally support the parent relationship and may be 
  * read only.</p>
  *
  * @see #supportsParent 
  * @see #isReadOnly
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.22 $
  */
public interface Node extends Cloneable {

    // W3C DOM complient node type codes
    
    /** Matches Element nodes */
    public static final short ANY_NODE = 0;
    /** Matches Element nodes */
    public static final short ELEMENT_NODE = 1;
    /** Matches elements nodes */
    public static final short ATTRIBUTE_NODE = 2;
    /** Matches elements nodes */
    public static final short TEXT_NODE = 3;
    /** Matches elements nodes */
    public static final short CDATA_SECTION_NODE = 4;
    /** Matches elements nodes */
    public static final short ENTITY_REFERENCE_NODE = 5;
    /** Matches elements nodes */
    //public static final short ENTITY_NODE = 6;
    /** Matches ProcessingInstruction */
    public static final short PROCESSING_INSTRUCTION_NODE = 7;
    /** Matches Comments nodes */
    public static final short COMMENT_NODE = 8;
    /** Matches Document nodes */
    public static final short DOCUMENT_NODE = 9;
    /** Matches DocumentType nodes */
    public static final short DOCUMENT_TYPE_NODE = 10;
    //public static final short DOCUMENT_FRAGMENT_NODE = 11;
    //public static final short NOTATION_NODE = 12;
    
    /** Matchs a Namespace Node - NOTE this differs from DOM */
    // XXXX: ????
    public static final short NAMESPACE_NODE = 13;
    
    /** Does not match any valid node */
    public static final short UNKNOWN_NODE = 14;
    
    /** The maximum number of node types for sizing purposes */
    public static final short MAX_NODE_TYPE = 14;
    
    
    /** <p><code>supportsParent</code> returns true if this node supports the 
      * parent relationship.</p>
      * 
      * <p>Some XML tree implementations are singly linked and only support
      * downward navigation through children relationships. 
      * The default case is that both parent and children relationships are
      * supported though for memory and performance reasons the parent
      * relationship may not be supported.
      * </p>
      *
      * @return true if this node supports the parent relationship
      * or false it is not supported
      */
    public boolean supportsParent();

    /** <p><code>getParent</code> returns the parent <code>Element</code> 
      * if this node supports the parent relationship or null if it is 
      * the root element or does not support the parent relationship.</p>
      *
      * <p>This method is an optional feature and may not be supported
      * for all <code>Node</code> implementations.</p>
      *
      * @return the parent of this node or null if it is the root of the 
      * tree or the parent relationship is not supported.
      */
    public Element getParent();

    /** <p><code>setParent</code> sets the parent relationship of
      * this node if the parent relationship is supported or does nothing
      * if the parent relationship is not supported.</p>
      *
      * <p>This method should only be called from inside an 
      * <code>Element</code> implementation method and is not intended for 
      * general use.</p>
      *
      * @param parent is the new parent of this node.
      */
    public void setParent(Element parent);
    

    /** <p><code>getDocument</code> returns the <code>Document</code>
      * that this <code>Node</code> is part of if this node supports
      * the parent relationship.</p>
      *
      * <p>This method is an optional feature and may not be supported
      * for all <code>Node</code> implementations.</p>
      *
      * @return the document of this node or null if this feature is not 
      * supported or the node is not associated with a <code>Document</code>
      */
    public Document getDocument();

    /** <p><code>setDocument</code> sets the document of this node if the 
      * parent relationship is supported or does nothing if the parent 
      * relationship is not supported.</p>
      *
      * <p>This method should only be called from inside a
      * <code>Document</code> implementation method and is not intended for 
      * general use.</p>
      *
      * @param document is the new document of this node.
      */
    public void setDocument(Document document);
    
    
    /** <p><code>isReadOnly</code> returns true if this node is read only
      * and cannot be modified. 
      * Any attempt to modify a read-only <code>Node</code> will result in 
      * an <code>UnsupportedOperationException</code> being thrown.</p>
      *
      * @return true if this <code>Node</code> is read only 
      * and cannot be modified otherwise false.
      */
    public boolean isReadOnly();

    /** <p><code>hasContent</code> returns true if this node is a Branch
      * (either an Element or a Document) and it contains at least one
      * content node such as a child Element or Text node.</p>
      *
      * @return true if this <code>Node</code> is a Branch
      * with a nodeCount() of one or more.
      */
    public boolean hasContent();
    

    
    /** <p><code>getName</code> returns the name of this node.
      * This is the XML local name of the element, attribute, entity or 
      * processing instruction. 
      * For CDATA and Text nodes this method will return null.</p>
      *
      * @return the XML name of this node
      */
    public String getName();    

    
   /** <p>Sets the text data of this node or this method will 
     * throw an <code>UnsupportedOperationException</code> if it is 
     * read-only.</p>
     *
     * @param name is the new name of this node
     */
    public void setName(String name);

    /** <p>Returns the text of this node.</p>
      *
      * @return the text for this node.
      */
    public String getText();
    
   /** <p>Sets the text data of this node or this method will 
     * throw an <code>UnsupportedOperationException</code> if it is 
     * read-only.</p>
     *
     * @param text is the new textual value of this node
     */
    public void setText(String text);
    
    /** Returns the XPath string-value of this node. 
      * The behaviour of this method is defined in the 
      * <a href="http://www.w3.org/TR/xpath">XPath specification</a>.
      *
      * @return the text from all the child Text and Element nodes appended 
      * together.
      */
    public String getStringValue();    
    
    /** <p>Returns the XPath expression which will return a node set
      * containing the given node such as /a/b/@c. No indexing will
      * be used to restrict the path if multiple elements with the
      * same name occur on the path.</p>
      *
      * @return the XPath expression which will return a nodeset
      * containing at least this node.
      */
    public String getPath();
    
    /** <p>Returns the relative XPath expression which will return a node set
      * containing the given node such as a/b/@c. No indexing will
      * be used to restrict the path if multiple elements with the
      * same name occur on the path.
      *
      * @param context is the parent context from which the relative path should 
      * start. If the context is null or the context is not an ancestor of
      * this node then the path will be absolute and start from the document and so 
      * begin with the '/' character.
      *
      * @return the XPath expression relative to the given context 
      * which will return a nodeset containing at least this node.
      */
    public String getPath(Element context);
    
    /** <p>Returns the XPath expression which will return a nodeset
      * of one node which is the current node. This method will use
      * the XPath index operator to restrict the path if
      * multiple elements with the same name occur on the path.</p>
      *
      * @return the XPath expression which will return a nodeset
      * containing just this node.
      */
    public String getUniquePath();
    
    /** <p>Returns the relative unique XPath expression from the given context
      * which will return a nodeset
      * of one node which is the current node. 
      * This method will use the XPath index operator to restrict the 
      * path if multiple elements with the same name occur on the path.
      * </p>
      *
      * @param context is the parent context from which the path should 
      * start. If the context is null or the context is not an ancestor of
      * this node then the path will start from the document and so 
      * begin with the '/' character.
      *
      * @return the XPath expression relative to the given context 
      * which will return a nodeset containing just this node.
      */
    public String getUniquePath(Element context);
    
    
    /** <p><code>asXML</code> returns the textual XML representation of this 
      * node.</p>
      *
      * @return the XML representation of this node
      */
    public String asXML();    

    /** <p><code>write</code> writes this node as the default XML 
      * notation for this node. If you wish to control the XML output
      * (such as for pretty printing, changing the indentation policy etc.) 
      * then please use {@link org.dom4j.io.XMLWriter} or its derivations.
      *
      * @param writer is the <code>Writer</code> to output the XML to
      */
    public void write(Writer writer) throws IOException;

    
    /** Returns the code according to the type of node. 
      * This makes processing nodes polymorphically much easier as the
      * switch statement can be used instead of multiple if (instanceof) 
      * statements.
      *
      * @return a W3C DOM complient code for the node type such as 
      * ELEMENT_NODE or ATTRIBUTE_NODE
      */
    public short getNodeType();

    /** @return the name of the type of node such as "Document", "Element", "Attribute" or "Text"
     */
    public String getNodeTypeName();
    
    
    /** <p>Removes this node from its parent if there is one. 
      * If this node is the root element of a document then it is removed
      * from the document as well.</p>
      *
      * <p>This method is useful if you want to remove
      * a node from its source document and add it to another document.
      * For example</p>
      *
      * <code>
      *     Node node = ...;
      *     Element someOtherElement = ...;
      *     someOtherElement.add( node.detach() );
      * </code>
      *
      * @return the node that has been removed from its parent node if 
      * any and its document if any.
      */
    public Node detach();
    
    
    
    /** <p><code>selectNodes</code> evaluates an XPath expression and returns 
      * the result as a <code>List</code> of <code>Node</code> instances or 
      * <code>String</code> instances depending on the XPath expression.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the list of <code>Node</code> or <code>String</code> instances 
      * depending on the XPath expression
      */
    public List selectNodes(String xpathExpression);
    
    /** <p><code>selectObject</code> evaluates an XPath expression and returns 
      * the result as an {@link Object}. The object returned can
      * either be a {@link List} of {@link Node} instances, a {@link Node} 
      * instance, a {@link String} or a {@link Number} instance depending on 
      * the XPath expression. 
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the value of the XPath expression as a
      * {@link List} of {@link Node} instances, a {@link Node} 
      * instance, a {@link String} or a {@link Number} instance depending on 
      * the XPath expression. 
      */
    public Object selectObject(String xpathExpression);
    
    /** <p><code>selectNodes</code> evaluates an XPath expression then
      * sorts the results using a secondary XPath expression
      * Returns a sorted <code>List</code> of <code>Node</code> instances.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @param comparisonXPathExpression is the XPath expression used
      *     to compare the results by for sorting
      * @return the list of <code>Node</code> instances 
      * sorted by the comparisonXPathExpression
      */
    public List selectNodes( 
        String xpathExpression, 
        String comparisonXPathExpression 
    );
    
    /** <p><code>selectNodes</code> evaluates an XPath expression then
      * sorts the results using a secondary XPath expression
      * Returns a sorted <code>List</code> of <code>Node</code> instances.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @param comparisonXPathExpression is the XPath expression used
      *     to compare the results by for sorting
      * @param removeDuplicates if this parameter is true then duplicate 
      *     values (using the comparisonXPathExpression) are removed from
      *     the result List.
      * @return the list of <code>Node</code> instances 
      * sorted by the comparisonXPathExpression
      */
    public List selectNodes(
        String xpathExpression, 
        String comparisonXPathExpression, 
        boolean removeDuplicates
    );
    
    /** <p><code>selectSingleNode</code> evaluates an XPath expression
      * and returns the result as a single <code>Node</code> instance.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the <code>Node</code> matching the XPath expression
      */
    public Node selectSingleNode(String xpathExpression);

    /** <p><code>valueOf</code> evaluates an XPath expression
      * and returns the textual representation of the results the XPath 
      * string-value of this node. 
      * The string-value for a given node type is defined in the 
      * <a href="http://www.w3.org/TR/xpath">XPath specification</a>.
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the string-value representation of the results of the XPath 
      * expression
      */
    public String valueOf(String xpathExpression);

    /** <p><code>numberValueOf</code> evaluates an XPath expression
      * and returns the numeric value of the XPath expression if the XPath
      * expression results in a number, or null if the result is not a number.
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the numeric result of the XPath expression or null
      * if the result is not a number.
      */
    public Number numberValueOf(String xpathExpression);

        
    /** <p><code>matches</code> returns true if evaluating the given
      * XPath expression on this node returns true or a non-empty node set.
      * So calling this method is equivalent to calling 
      * <code>&lt;xsl:if test="xpathExpression"/&gt;</code>
      * in XSLT.</p>
      *
      * @param xpathExpression is an XPath expression
      * @return true if the given node matches this XSLT Pattern
      */
    public boolean matches(String xpathExpression);

    /** <p><code>createXPath</code> creates an XPath object for
      * the given xpathExpression.
      * The XPath object allows the variable context to be specified.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return an XPath object represeting the given expression
      * @throws InvalidXPathException if the XPath expression is invalid
      */
    public XPath createXPath(String xpathExpression) throws InvalidXPathException;

    /** <p><code>asXPathResult</code> returns a version of this node which is
      * capable of being an XPath result. 
      * The result of an XPath expression should always support the parent 
      * relationship, whether the original XML tree was singly or doubly linked.
      * If the node does not support the parent relationship then a new node
      * will be created which is linked to its parent and returned.
      *
      * @return a <code>Node</code> which supports the parent relationship
      */
    public Node asXPathResult(Element parent);

    
    /** <p><code>accept</code> is the method used in the Visitor Pattern.</p>
      *
      * @param visitor is the visitor in the Visitor Pattern
      */
    public void accept(Visitor visitor);

    
    
    /** <p><code>clone</code> will return a deep clone or if this node is
      * read-only then clone will return the same instance.
      *
      * @@return a deep clone of myself or myself if I am read only.
      */
    public Object clone();
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
 * $Id: Node.java,v 1.22 2001/09/15 14:55:14 jstrachan Exp $
 */