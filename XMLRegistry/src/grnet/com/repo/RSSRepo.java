/**
 * 
 */
package grnet.com.repo;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author vogias
 * 
 */
public class RSSRepo {
	String name, url, xslURLstr, protocol;

	/**
	 * @return the name
	 */
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @return the url
	 */
	@XmlElement
	public String getUrl() {
		return url;
	}

	/**
	 * @return the xslURLstr
	 */
	@XmlElement
	public String getXslURLstr() {
		return xslURLstr;
	}

	/**
	 * @return the protocol
	 */
	@XmlElement
	public String getProtocol() {
		return "RSS";
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param xslURLstr
	 *            the xslURLstr to set
	 */
	public void setXslURLstr(String xslURLstr) {
		this.xslURLstr = xslURLstr;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
