/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */
package org.dom4j.tree;

import org.dom4j.Node;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * <code>ContentListFacade</code> represents a facade of the content of a
 * {@link org.dom4j.Branch} which is returned via calls to the {@link
 * org.dom4j.Branch#content}  method to allow users to modify the content of a
 * {@link org.dom4j.Branch} directly using the {@link List} interface. This list
 * is backed by the branch such that changes to the list will be reflected in
 * the branch and changes to the branch will be reflected in this list.
 * </p>
 *
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.11 $
 */
public class ContentListFacade<T extends Node> extends AbstractList<T> {

	/**
	 * The content of the Branch which is modified if I am modified
	 */
	private List<T> branchContent;
	/**
	 * The <code>AbstractBranch</code> instance which owns the content
	 */
	private AbstractBranch branch;

	public ContentListFacade(AbstractBranch branch, List<T> branchContent) {
		this.branch = branch;
		this.branchContent = branchContent;
	}

	@Override
	public boolean add(T node) {
		branch.childAdded(node);

		return branchContent.add(node);
	}

	@Override
	public void add(int index, T node) {
		branch.childAdded(node);
		branchContent.add(index, node);
	}

	@Override
	public T set(int index, T node) {
		branch.childAdded(node);

		return branchContent.set(index, node);
	}

	public boolean remove(T node) {
		branch.childRemoved(node);

		return branchContent.remove(node);
	}

	@Override
	public T remove(int index) {
		T node = branchContent.remove(index);

		if (node != null) {
			branch.childRemoved(node);
		}

		return node;
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		int count = branchContent.size();

		for (Iterator<? extends T> iter = collection.iterator(); iter.hasNext(); count++) {
			add(iter.next());
		}

		return count == branchContent.size();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> collection) {
		int count = branchContent.size();

		for (Iterator<? extends T> iter = collection.iterator(); iter.hasNext(); count--) {
			add(index++, iter.next());
		}

		return count == branchContent.size();
	}

	@Override
	public void clear() {
		for (T node : this) {
			branch.childRemoved(node);
		}

		branchContent.clear();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object object : c) {
			if (object instanceof Node) {
				branch.childRemoved((Node) object);
			}
		}

		return branchContent.removeAll(c);
	}

	public int size() {
		return branchContent.size();
	}

	@Override
	public boolean isEmpty() {
		return branchContent.isEmpty();
	}

	public boolean contains(T node) {
		return branchContent.contains(node);
	}

	@Override
	public Node[] toArray() {
		return (Node[]) branchContent.toArray();
	}

	public Node[] toArray(Node[] a) {
		return branchContent.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return branchContent.containsAll(c);
	}

	public T get(int index) {
		return branchContent.get(index);
	}

	public int indexOf(T node) {
		return branchContent.indexOf(node);
	}

	public int lastIndexOf(T node) {
		return branchContent.lastIndexOf(node);
	}

	protected List<T> getBackingList() {
		return branchContent;
	}
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://dom4j.sourceforge.net
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
