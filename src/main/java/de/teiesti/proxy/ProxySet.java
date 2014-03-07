package de.teiesti.proxy;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ProxySet<Proxy, Subject> implements Set<Proxy> {

	private Set<Subject> subjects;
	private Mapper<Proxy, Subject> mapper;

	public ProxySet(Set<Subject> subjects, Mapper<Proxy, Subject> mapper) {
		if (subjects == null)
			throw new IllegalArgumentException("subjects == null");
		if (mapper == null)
			throw new IllegalArgumentException("mapper == null");

		this.subjects = subjects;
		this.mapper = mapper;
	}

	@Override
	public boolean add(Proxy e) {
		return subjects.add(mapper.getSubject(e));
	}

	@Override
	public boolean addAll(Collection<? extends Proxy> c) {
		// TODO there is possibly a solution with more performance
		boolean result = false;
		for (Proxy p : c) {
			result |= add(p);
		}
		return result;
	}

	@Override
	public void clear() {
		subjects.clear();
	}

	@Override
	public boolean contains(Object o) {
		if (o == null || mapper.getProxyClass().isAssignableFrom(o.getClass())) {
			/*
			 * Attention: Black magic! The if-condition checks, if the given parameter can be assigned to a variable of
			 * type Proxy. Therefore the parameter must be null or an instance of Proxy. We must suppress the warning
			 * before the allocation, because this cast check is not ordinary.
			 */

			@SuppressWarnings("unchecked")
			Proxy p = (Proxy) o;

			return subjects.contains(mapper.getSubject(p));
		} else return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO there is possibly a more performant solution
		for (Object o : c)
			if (!contains(o)) return false;
		return true;
	}

	@Override
	public boolean isEmpty() {
		return subjects.isEmpty();
	}

	@Override
	public Iterator<Proxy> iterator() {
		return new ProxyIterator<Proxy, Subject>(subjects.iterator(), mapper);
	}

	@Override
	public boolean remove(Object o) {
		if (o == null || mapper.getProxyClass().isAssignableFrom(o.getClass())) {
			/*
			 * Attention: Black magic! The if-condition checks, if the given parameter can be assigned to a variable of
			 * type Proxy. Therefore the parameter must be null or an instance of Proxy. We must suppress the warning
			 * before the allocation, because this cast check is not ordinary.
			 */

			@SuppressWarnings("unchecked")
			Proxy p = (Proxy) o;

			return subjects.remove(mapper.getSubject(p));
		} else return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO there is possibly a more performant solution
		boolean result = false;
		for (Object p : c) {
			result |= remove(p);
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO there is possibly a more performant solution
		Iterator<Proxy> it = iterator();
		boolean result = false;
		while (it.hasNext()) {
			if (!c.contains(it.next())) {
				result = true;
				it.remove();
			}
		}
		return result;
	}

	@Override
	public int size() {
		return subjects.size();
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for (Subject s : subjects) {
			result[i++] = mapper.getProxy(s);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (!a.getClass().getComponentType().isAssignableFrom(mapper.getProxyClass())) {    // black magic
			throw new ArrayStoreException();
		}

		if (a.length < size())
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), size());            // needed two hours to resolve a bug

		int i = 0;
		for (Subject s : subjects) {
			a[i++] = (T) mapper.getProxy(s);
		}
		return a;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("[");

		Iterator<Proxy> it = this.iterator();
		if (it.hasNext()) result.append(it.next());
		while (it.hasNext()) {
			result.append(", ");
			result.append(it.next());
		}

		result.append("]");

		return result.toString();
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (Proxy p : this)
			result += p == null ? 0 : p.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Set<?>) {
			Set<?> other = (Set<?>) obj;
			return this.size() == other.size() && this.containsAll(other);
		}
		return false;
	}

}
