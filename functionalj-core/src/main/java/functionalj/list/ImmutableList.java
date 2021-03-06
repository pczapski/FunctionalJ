//  ========================================================================
//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package functionalj.list;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import functionalj.stream.StreamPlus;
import functionalj.stream.Streamable;
import lombok.val;

@SuppressWarnings("javadoc")
public final class ImmutableList<DATA> implements FuncList<DATA> {
    
    private final static ImmutableList<?> EMPTY = new ImmutableList<>(Collections.emptyList());
    
    @SuppressWarnings("unchecked")
    public static final <T> ImmutableList<T> empty() {
        return (ImmutableList<T>)EMPTY;
    }
    public static <T> ImmutableList<T> from(Collection<T> data) {
        if (data instanceof ImmutableList)
            return (ImmutableList<T>)data;
        if (data == null)
            return empty();
        
        return new ImmutableList<T>(data);
    }
    @SafeVarargs
    public static <T> ImmutableList<T> of(T ... data) {
        return new ImmutableList<>(Arrays.asList(data));
    }
    public static <T> ImmutableList<T> from(T[] datas) {
        return new ImmutableList<>(Arrays.asList(datas));
    }
    public static <T> ImmutableList<T> from(Streamable<T> streamable) {
        if (streamable instanceof ImmutableList)
            return (ImmutableList<T>)streamable;
        if (streamable == null)
            return ImmutableList.empty();
        
        return new ImmutableList<T>(streamable.toJavaList());
    }
    public static <T> ImmutableList<T> from(Stream<T> stream) {
        return new ImmutableList<T>(stream.collect(Collectors.toList()));
    }
    public static <T> ImmutableList<T> from(ReadOnlyList<T> readOnlyList) {
        if (readOnlyList instanceof ImmutableList)
            return (ImmutableList<T>)readOnlyList;
        if (readOnlyList == null)
            return ImmutableList.empty();
        
        return new ImmutableList<T>(readOnlyList.toJavaList());
    }
    public static <T> ImmutableList<T> from(FuncList<T> funcList) {
        if (funcList instanceof ImmutableList)
            return (ImmutableList<T>)funcList;
        if (funcList == null)
            return ImmutableList.empty();
        
        return new ImmutableList<T>(funcList.toJavaList());
    }
    @SafeVarargs
    public static <T> ImmutableList<T> listOf(T ... data) {
        return new ImmutableList<T>(Arrays.asList(data));
    }
    
    private final List<DATA> data;
    private final boolean    isLazy;
    
    public ImmutableList(Collection<DATA> data) {
        this(data, true);
    }
    public ImmutableList(Collection<DATA> data, boolean isLazy) {
        this.isLazy = isLazy;
        if (data == null) {
            this.data = Collections.emptyList();
        } else if (data instanceof ImmutableList) {
            this.data = ((ImmutableList<DATA>)data).data;
        } else {
            val list = new ArrayList<DATA>();
            data.forEach(list::add);
            this.data = unmodifiableList(list);
        }
    }
    
    @Override
    public StreamPlus<DATA> stream() {
        return StreamPlus.from(data.stream());
    }
    
    public boolean isLazy() {
        return isLazy;
    }
    
    public boolean isEager() {
        return !isLazy;
    }
    
    public FuncList<DATA> lazy() {
        if (isLazy)
            return this;
        
        return new ImmutableList<DATA>(data, true);
    }
    public FuncList<DATA> eager() {
        if (!isLazy)
            return this;
        
        return new ImmutableList<DATA>(data, false);
    }
    
    @Override
    public ImmutableList<DATA> toImmutableList() {
        return this;
    }
    
    @Override
    public int size() {
        return data.size();
    }
    
    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
    
    @Override
    public <TARGET> TARGET[] toArray(TARGET[] seed) {
        return data.toArray(seed);
    }
    
    @Override
    public DATA get(int index) {
        return data.get(index);
    }
    
    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }
    
    @Override
    public ListIterator<DATA> listIterator() {
        return data.listIterator();
    }
    
    @Override
    public ListIterator<DATA> listIterator(int index) {
        return data.listIterator();
    }
    
    // TODO - Make sure all method from ReadOnlyList is copied here and goes directly to data.
    
    @Override
    public String toString() {
        return this.data.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        return this.data.equals(o);
    }
    
    @Override
    public int hashCode() {
        return this.data.hashCode();
    }
    
}
