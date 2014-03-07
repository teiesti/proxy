package de.teiesti.proxy;

import java.util.Iterator;

/**
 * A {@code ProxyIterator} iterates over the associated proxies in the same order as a given other iterator iterates
 * over the subjects. In detail: Assume you have an iterator iterating over some subject. Now you encapsulate it - by
 * the help of a {@link Mapper} - within a proxy iterator. The resulting iterator behaves exactly like the given
 * original except that it returns proxies pointing to the subjects.<br />
 * Depending on the {@link Mapper} implementation the {@code ProxyIterator} is very fast, because a subject is not
 * mapped until it is needed.
 *
 * @param <Proxy> the type of the proxy
 * @param <Subject> the type of the subject
 */
public class ProxyIterator<Proxy, Subject> implements Iterator<Proxy> {

	private Iterator<Subject> subjects;
	private Mapper<Proxy, Subject> mapper;

	/**
	 * Creates a {@code ProxyIterator} from a given subject iterator with the help of a {@link Mapper}. The mapper is
	 * used to create a proxy for each subject (which is needed).
	 *
	 * @param subjects an iterator over some subjects
	 * @param mapper a mapper which maps the subjects to the proxies
	 */
	public ProxyIterator(Iterator<Subject> subjects, Mapper<Proxy, Subject> mapper) {
		if (subjects == null)
			throw new IllegalArgumentException("subjects == null");
		if (mapper == null)
			throw new IllegalArgumentException("mapper == null");

		this.subjects = subjects;
		this.mapper = mapper;
	}

	/**
	 * Returns if this iterator has a next proxy. This method return {@code true} if and only if the underlying
	 * iterator has a next subject.
	 *
	 * @return if this iterator has a next proxy
	 */
	@Override
	public boolean hasNext() {
		return subjects.hasNext();
	}

	/**
	 * Returns the next proxy in this iterator. This is done in two steps: First the next subject is taken from the
	 * underlying iterator. Second the taken subject is converted to a proxy using the
	 * {@link Mapper#getProxy(Object) }-method.
	 * @return the next proxy
	 */
	@Override
	public Proxy next() {
		return mapper.getProxy(subjects.next());
	}

	/**
	 * Removes - if supported - the current proxy from the associated collection. This method is transparent which
	 * means that calling this method removes the subject from the collection which is associated to the underlying
	 * subject iterator.
	 */
	@Override
	public void remove() {
		subjects.remove();
	}

}
