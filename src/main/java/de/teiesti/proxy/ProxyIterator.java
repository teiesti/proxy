package de.teiesti.proxy;

import java.util.Iterator;

public class ProxyIterator<Proxy, Subject> implements Iterator<Proxy> {

	private Iterator<Subject> subjects;
	private Mapper<Proxy, Subject> mapper;

	public ProxyIterator(Iterator<Subject> subjects, Mapper<Proxy, Subject> mapper) {
		if (subjects == null)
			throw new IllegalArgumentException("subjects == null");
		if (mapper == null)
			throw new IllegalArgumentException("mapper == null");

		this.subjects = subjects;
		this.mapper = mapper;
	}

	@Override
	public boolean hasNext() {
		return subjects.hasNext();
	}

	@Override
	public Proxy next() {
		return mapper.getProxy(subjects.next());
	}

	@Override
	public void remove() {
		subjects.remove();
	}

}
