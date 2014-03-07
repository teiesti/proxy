package de.teiesti.proxy;

public interface Mapper<Proxy, Subject> {

	public Proxy getProxy(Subject subject);

	public Subject getSubject(Proxy proxy);

	public Class<?> getProxyClass();

	public Class<?> getSubjectClass();

}
