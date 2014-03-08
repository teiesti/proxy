package de.teiesti.proxy;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * A {@code ProxySet} provides a "proxy view" to a whole set of subjects. That means that a proxy set which
 * encapsulates a set of subject by the help of a {@link Mapper}, provides almost the same functionality as a trivial
 * set of proxies which is generated in the following manner:<br /><br />
 *
 * <pre>{@code
 * Set<Proxy> proxies = new ...;
 * for (Subject s : subjects)
 *    proxies.add(mapper.getProxy(s));
 * }</pre>
 *
 * But a proxy set is - depending on the {@link Mapper} implementation - much faster,
 * because a proxy is only mapped to a subject if needed.<br /><br />
 *
 * Be careful: The {@code ProxySet} is - in contrast to the trivial alternative above - transparent to changes which
 * means that
 * changing this set changes the underlying set of subjects and vice versa.
 *
 * @param <Proxy> the type of the proxy
 * @param <Subject> the type of the subject
 */
public class ProxySet<Proxy, Subject> implements Set<Proxy> {

	private Set<Subject> subjects;
	private Mapper<Proxy, Subject> mapper;

	/**
	 * Creates a new {@code ProxySet} which encapsulates a given set of subjects by the help of a {@link Mapper}.
	 *
	 * @param subjects the set of subjects
	 * @param mapper a mapper mapping subjects to proxies
	 */
	public ProxySet(Set<Subject> subjects, Mapper<Proxy, Subject> mapper) {
		if (subjects == null)
			throw new IllegalArgumentException("subjects == null");
		if (mapper == null)
			throw new IllegalArgumentException("mapper == null");

		this.subjects = subjects;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: This method modifies the underlying set of subjects.
	 */
	@Override
	public boolean add(Proxy e) {
		return subjects.add(mapper.getSubject(e));
	}

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: This method modifies the underlying set of subjects.
	 */
	@Override
	public boolean addAll(Collection<? extends Proxy> c) {
		// TODO you could possibly check whether this given collection is a proxy collection and be faster
		boolean result = false;
		for (Proxy p : c) {
			result |= add(p);
		}
		return result;
	}

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: This method modifies the underlying set of subjects.
	 */
	@Override
	public void clear() {
		subjects.clear();
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO there is possibly a more performant solution
		for (Object o : c)
			if (!contains(o)) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return subjects.isEmpty();
	}

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: Invoking the {@link Iterator#remove()}-method affects the underlying set of subjects, too.
	 */
	@Override
	public Iterator<Proxy> iterator() {
		return new ProxyIterator<Proxy, Subject>(subjects.iterator(), mapper);
	}

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: This method modifies the underlying set of subjects.
	 */
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

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: This method modifies the underlying set of subjects.
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO there is possibly a more performant solution
		boolean result = false;
		for (Object p : c) {
			result |= remove(p);
		}
		return result;
	}

	/**
	 * {@inheritDoc}<br /><br />
	 * Note: This method modifies the underlying set of subjects.
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return subjects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for (Subject s : subjects) {
			result[i++] = mapper.getProxy(s);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = 0;
		for (Proxy p : this)
			result += p == null ? 0 : p.hashCode();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
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
