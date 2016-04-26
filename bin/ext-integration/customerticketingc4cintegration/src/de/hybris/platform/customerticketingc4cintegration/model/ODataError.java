/**
 *
 */
package de.hybris.platform.customerticketingc4cintegration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;


/**
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ODataError
{
	@JsonProperty
	private String code;
	@JsonProperty
	private Message message;

	public String getCode()
	{
		return code;
	}

	public void setCode(final String code)
	{
		this.code = code;
	}

	public Message getMessage()
	{
		return message;
	}

	public void setMessage(final Message message)
	{
		this.message = message;
	}

	@JsonRootName(value = "message")
	public static class Message
	{
		@JsonProperty
		private String lang;
		@JsonProperty
		private String value;

		public String getLang()
		{
			return lang;
		}

		public void setLang(final String lang)
		{
			this.lang = lang;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(final String value)
		{
			this.value = value;
		}
	}
}
