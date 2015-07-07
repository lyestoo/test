/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: BackedList.java,v 1.3 2001/01/26 11:21:54 jstrachan Exp $
 */

package org.dom4j.tree;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.CDATA;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.Text;

/** <p><code>BackedList</code> represents a list of content
  * of a {@link Branch}. Changes to the list will
  * be reflected in the branch, though changes to the branch will not 
  * be reflected in this list.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision: 1.3 $
  */
public class BackedList extends ArrayList {

    /** The content of the Branch which is modified if I am modified */
    private List branchContent;

    /** The <code>AbstractBranch</code> instance which owns the content */
    private AbstractBranch branch;

    
    public BackedList(AbstractBranch branch,List branchContent) { 
        this( branch, branchContent, branchContent.size() );
    }
    
    public BackedList(AbstractBranch branch,List branchContent,int capacity) { 
        super( capacity );
        this.branch = branch;
        this.branchContent = branchContent;
    }
    
    public BackedList(AbstractBranch branch,List branchContent,List initialContent) { 
        super( initialContent );
        this.branch = branch;
        this.branchContent = branchContent;
    }
    
    public boolean add(Object object) {
        branch.addNode( asNode( object ) );
        return super.add(object);
    }
    
    public void add(int index, Object object) {
        int realIndex = branchContent.indexOf( get(index) );
        if ( realIndex < 0 ) {
            realIndex = ( index == 0 ) ? 0 : Integer.MAX_VALUE;
        }
        if ( realIndex < branchContent.size() ) {
            branchContent.add(realIndex, object);
        }
        else {
            branchContent.add(object);
        }
        branch.childAdded( asNode( object ) );
        super.add(index, object);
    }
    
    public Object set(int index, Object object) {
        int realIndex = branchContent.indexOf( get(index) );
        if ( realIndex < 0 ) {
            realIndex = ( index == 0 ) ? 0 : Integer.MAX_VALUE;
        }
        if ( realIndex < branchContent.size() ) {
            branchContent.set(realIndex, object);
        }
        else {
            branchContent.add(object);
        }
        branch.childAdded( asNode( object ) );
        return super.set(index, object);
    }
    
    public boolean remove(Object object) {
        branch.removeNode( asNode( object ) );
        return super.remove(object);
    }

    public Object remove(int index) {
        Object object = super.remove(index);
        if ( object != null ) {
            branch.removeNode( asNode( object ) );
        }
        return object;
    }

    public boolean addAll(Collection collection) {
        ensureCapacity(size() + collection.size());
        
        int count = size();
        for (Iterator iter = collection.iterator(); iter.hasNext(); count-- ) {
            add(iter.next());
        }
        return count != 0;
    }
    
    public boolean addAll(int index, Collection collection) {
        ensureCapacity(size() + collection.size());
        
        int count = size();
        for (Iterator iter = collection.iterator(); iter.hasNext(); count-- ) {
            add(index++, iter.next());
        }
        return count != 0;
    }
    
    public void clear() {
        for ( Iterator iter = iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            branchContent.remove(object);
            branch.childRemoved( asNode( object ) );
        }
        super.clear();
    }

    /** Performs a local addition which is not forward through to the 
      * Branch or backing list 
      */
    public void addLocal(Object object) {
        super.add(object);
    }
    
    protected Node asNode(Object object) {
        if (object instanceof Node) {
            return (Node) object;
        }
        else {
            throw new IllegalAddException( "This list must contain instances of Node. Invalid type: "+ object );
        }
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
 * $Id: BackedList.java,v 1.3 2001/01/26 11:21:54 jstrachan Exp $
 */