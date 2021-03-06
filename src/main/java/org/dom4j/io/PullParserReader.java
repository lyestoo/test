/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: PullParserReader.java,v 1.3 2001/06/25 15:57:32 jstrachan Exp $
 */

package org.dom4j.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.QName;

import org.xml.sax.InputSource;

import xpp.EndTag;
import xpp.StartTag;
import xpp.XmlPullParser;
import xpp.XmlPullParserException;

/** <p><code>PullParserReader</code> is a Reader of DOM4J documents that 
  * uses the fast 
  * <a href="http://www.extreme.indiana.edu/soap/xpp/">XML Pull Parser</a>.
  * It does not currently support comments, CDATA or ProcessingInstructions or
  * validation but it is very fast for use in SOAP style environments.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 1.3 $
  */
public class PullParserReader {

    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory;
    
    /** <code>XmlPullParser</code> used to parse XML */
    private XmlPullParser pullParser = new XmlPullParser();
    
    /** <code>StartTag</code> is the start tag */
    private StartTag startTag = new StartTag();
    
    /** <code>EndTag</code> is the start tag */
    private EndTag endTag = new EndTag();
    
    /** DispatchHandler to call when each <code>Element</code> is encountered */
    private DispatchHandler dispatchHandler;
 
        
    
    public PullParserReader() {
    }

    public PullParserReader(DocumentFactory factory) {
        this.factory = factory;
    }

    
    
        
    /** <p>Reads a Document from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      * @throws MalformedURLException if a URL could not be made for the given File
      */
    public Document read(File file) throws DocumentException, IOException {
        String systemID = file.getAbsolutePath();
        return read( new BufferedReader( new FileReader( file ) ), systemID );
    }
    
    /** <p>Reads a Document from the given <code>URL</code></p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(URL url) throws DocumentException, IOException {
        String systemID = url.toExternalForm();
        return read( createReader( url.openStream() ), systemID);
    }
    
    /** <p>Reads a Document from the given URL or filename.</p>
      *
      * <p>
      * If the systemID contains a <code>':'</code> character then it is
      * assumed to be a URL otherwise its assumed to be a file name.
      * If you want finer grained control over this mechansim then please
      * explicitly pass in either a {@link URL} or a {@link File} instance
      * instead of a {@link String} to denote the source of the document.
      * </p>
      *
      * @param systemID is a URL for a document or a file name.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      * @throws MalformedURLException if a URL could not be made for the given File
      */
    public Document read(String systemID) throws DocumentException, IOException {
        if ( systemID.indexOf( ':' ) >= 0 ) {
            // lets assume its a URL
            return read(new URL(systemID));
        }
        else {
            // lets assume that we are given a file name
            return read( new File(systemID) );
        }
    }

    /** <p>Reads a Document from the given stream</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(InputStream in) throws DocumentException, IOException {
        return read( createReader( in ) );
    }

    /** <p>Reads a Document from the given <code>Reader</code></p>
      *
      * @param reader is the reader for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(Reader reader) throws DocumentException, IOException {
        pullParser.setInput(reader);
        return parseDocument();
    }

    /** <p>Reads a Document from the given array of characters</p>
      *
      * @param text is the text to parse
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(char[] text) throws DocumentException, IOException {
        pullParser.setInput(text);
        return parseDocument();
    }

    /** <p>Reads a Document from the given stream</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @param systemID is the URI for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(InputStream in, String systemID) throws DocumentException, IOException {
        return read( createReader( in ), systemID );
    }

    /** <p>Reads a Document from the given <code>Reader</code></p>
      *
      * @param reader is the reader for the input
      * @param systemID is the URI for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(Reader reader, String systemID) throws DocumentException, IOException {
        Document document = read( reader );
        document.setName( systemID );
        return document;
    }

    
    // Properties
    //-------------------------------------------------------------------------                
        
    /** @return the <code>DocumentFactory</code> used to create document objects
      */
    public DocumentFactory getDocumentFactory() {
        if (factory == null) {
            factory = DocumentFactory.getInstance();
        }
        return factory;
    }

    /** <p>This sets the <code>DocumentFactory</code> used to create new documents.
      * This method allows the building of custom DOM4J tree objects to be implemented
      * easily using a custom derivation of {@link DocumentFactory}</p>
      *
      * @param factory <code>DocumentFactory</code> used to create DOM4J objects
      */
    public void setDocumentFactory(DocumentFactory factory) {
        this.factory = factory;
    }

    
    /** Adds the <code>ElementHandler</code> to be called when the 
      * specified path is encounted.
      *
      * @param path is the path to be handled
      * @param handler is the <code>ElementHandler</code> to be called
      * by the event based processor.
      */
    public void addHandler(String path, ElementHandler handler) {
        getDispatchHandler().addHandler(path, handler);   
    }
    
    /** Removes the <code>ElementHandler</code> from the event based
      * processor, for the specified path.
      *
      * @param path is the path to remove the <code>ElementHandler</code> for.
      */
    public void removeHandler(String path) {
        getDispatchHandler().removeHandler(path);   
    }
    
    /** When multiple <code>ElementHandler</code> instances have been 
      * registered, this will set a default <code>ElementHandler</code>
      * to be called for any path which does <b>NOT</b> have a handler
      * registered.
      * @param handler is the <code>ElementHandler</code> to be called
      * by the event based processor.
      */
    public void setDefaultHandler(ElementHandler handler) {
        getDispatchHandler().setDefaultHandler(handler);   
    }
    
    // Implementation methods    
    //-------------------------------------------------------------------------                    
    protected Document parseDocument() throws DocumentException, IOException {
        Document document = getDocumentFactory().createDocument();
        try {
            Branch branch = document;
            while (true) {
                int type = pullParser.next();
                switch (type) {
                    case XmlPullParser.END_DOCUMENT: {
                        return document;
                    }
                    case XmlPullParser.CONTENT: {
                        String text = pullParser.readContent();
                        addText( branch, text );
                        break;
                    }
                    case XmlPullParser.START_TAG: {
                        pullParser.readStartTag( startTag );
                        branch = addElement( branch, startTag );
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        pullParser.readEndTag( endTag );
                        branch = branch.getParent();
                        if ( branch == null ) {
                            branch = document;
                        }
                        break;
                    }
                    default: {
                        throw new DocumentException( "Error: unknown PullParser type: " + type );
                    }
                }
            }
        }
        catch (XmlPullParserException e) {
            throw new DocumentException(e.getMessage(), e);
        }
    }
    
    protected Element addElement( Branch branch, StartTag startTag ) {
        QName qname = getQName( startTag.getQName(), startTag.getUri() );
        int attributeCount = startTag.getLength();
        //Element element = factory.createElement( qname, attributeCount );
        Element element = factory.createElement( qname );
        for ( int i = 0; i < attributeCount; i++ ) {
            String name = startTag.getRawName(i);
            String uri = startTag.getURI(i);
            String value = startTag.getValue(i);
            QName attributeQname = getQName( name, uri );
            element.addAttribute( attributeQname, value );
        }
        branch.add( element );
        return element;
    }
    
    protected void addText( Branch branch, String text ) throws DocumentException {
        if ( branch instanceof Element ) {
            Element element = (Element) branch;
            element.addText( text );
        }
        else {
            throw new DocumentException( "Cannot have text content outside of the root document" );
        }
    }
    
    protected QName getQName( String qname, String uri) {
        return factory.createQName( qname, uri );
    }
    
    protected DispatchHandler getDispatchHandler() {
        if (dispatchHandler == null) {
            dispatchHandler = new DispatchHandler();
        }
        return dispatchHandler;   
    }
    
    protected void setDispatchHandler(DispatchHandler dispatchHandler) {
        this.dispatchHandler = dispatchHandler;
    }
    
    /** Factory method to create a Reader from the given InputStream.
     */
    protected Reader createReader(InputStream in) throws IOException {
        return new BufferedReader( new InputStreamReader( in ) );
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
 * $Id: PullParserReader.java,v 1.3 2001/06/25 15:57:32 jstrachan Exp $
 */
