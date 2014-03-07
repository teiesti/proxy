package de.teiesti.proxy;

/**
 * A {@code Mapper} converts a generic proxy object into its subject and vice-versa. In this way, mappers provide an
 * abstract view to the relationship between proxies and subjects which is needed for high-level algorithms and data
 * structures on proxies.
 *
 * @param <Proxy> the type of the proxy
 * @param <Subject> the type of the subject
 */
public interface Mapper<Proxy, Subject> {

	/**
	 * Returns a proxy object associated to the given subject. Among multiple method calls with the same subject the
	 * returned proxy must not be the same. However, different proxies for the same subject must be equal (see
	 * {@link Object#equals(Object)}).
	 *
	 * @param subject the subject to convert into its proxy
	 * @return a proxy object associated to the given subject
	 */
	public Proxy getProxy(Subject subject);

	/**
	 * Returns the subject the given proxy is associated to. Because one proxy always points to a single subject,
	 * this method returns the same object among different method calls with the same (or - see
	 * {@link Mapper#getProxy(Object)} above - an equal) proxy.
	 *
	 * @param proxy the proxy to convert into its subject
	 * @return the subject the given proxy is associated to
	 */
	public Subject getSubject(Proxy proxy);

	/**
	 * Returns the class of the proxies this mapper works with. This method is needed by some algorithms because one
	 * cannot obtain the class of a generic type.
	 *
	 * @return the class of the proxies this mapper works with
	 */
	public Class<?> getProxyClass();

	/**
	 * Returns the class of the proxies this mapper works with. This method is needed by some algorithms because one
	 * cannot obtain the class of a generic type.
	 *
	 * @return the class of the proxies this mapper works with
	 */
	public Class<?> getSubjectClass();

}
