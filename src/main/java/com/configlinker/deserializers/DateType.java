package com.configlinker.deserializers;


import com.configlinker.IDeserializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public enum DateType
{
	MILLISECONDS("", DateType.Milliseconds.class),
	SECONDS("", DateType.Seconds.class),
	YEAR("yyyy", DateType.Year.class),
	DATE("yyyy-MM-dd", DateType.DateOnly.class),
	TIME("HH:mm:ss", DateType.TimeOnly.class),
	DATE_TIME("yyyy-MM-dd'T'HH:mm:ss", DateType.DateTime.class),
	DATE_TIME_ZONE("yyyy-MM-dd'T'HH:mm:ssZ", DateType.DateTimeZone.class),
	RFC_3339("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", TimestampRFC_3339.class),
	RFC_822_1123("EEE, dd MMM yyyy HH:mm:ss zz", TimestampRFC_822_1123.class),
	RFC_850_1036("EEEEE, dd-MMM-yy HH:mm:ss zz", TimestampRFC_850_1036.class),;
	
	private final String pattern;
	public final Class<? extends IDeserializer<Date>> deserializerClass;
	
	DateType(String pattern, Class<? extends IDeserializer<Date>> deserializerClass)
	{
		this.pattern = pattern;
		this.deserializerClass = deserializerClass;
	}
	
	// --------------------------------------------------------------------------------
	
	static private abstract class AbstractDateTimeParser implements IDeserializer<Date>
	{
		private DateType dateType;
		
		private AbstractDateTimeParser(DateType dateType)
		{
			this.dateType = dateType;
		}
		
		@Override
		public Date deserialize(String rawValue)
		{
			try
			{
				return new SimpleDateFormat(dateType.pattern).parse(rawValue);
			}
			catch (ParseException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Milliseconds from epoch.
	 */
	static final public class Milliseconds extends AbstractDateTimeParser
	{
		private Milliseconds()
		{
			super(DateType.MILLISECONDS);
		}
		
		@Override
		public Date deserialize(String rawValue)
		{
			return new Date(Long.parseLong(rawValue));
		}
	}
	
	/**
	 * Seconds from epoch.
	 */
	static final public class Seconds extends AbstractDateTimeParser
	{
		private Seconds()
		{
			super(DateType.SECONDS);
		}
		
		@Override
		public Date deserialize(String rawValue)
		{
			return new Date(Long.parseLong(rawValue) * 1000);
		}
	}
	
	/**
	 * <p>A year in literally form.
	 * <p>Example: {@code 2001} or {@code 18} or {@code 184} or {@code 1542}.
	 */
	static final public class Year extends AbstractDateTimeParser
	{
		private Year()
		{
			super(DateType.YEAR);
		}
	}
	
	/**
	 * <p>Date as "yyyy-MM-dd".
	 * <p>Example: {@code '2001-07-04'}.
	 */
	static final public class DateOnly extends AbstractDateTimeParser
	{
		private DateOnly()
		{
			super(DateType.DATE);
		}
	}
	
	/**
	 * <p>Time as "HH:mm:ss"
	 * <p>Example: {@code '08:56:32'}.
	 */
	static final public class TimeOnly extends AbstractDateTimeParser
	{
		private TimeOnly()
		{
			super(DateType.TIME);
		}
	}
	
	/**
	 * <p>Date and time as "yyyy-MM-dd'T'HH:mm:ss".
	 * <p>Example: {@code '2001-07-04T12:08:56'}.
	 */
	static final public class DateTime extends AbstractDateTimeParser
	{
		private DateTime()
		{
			super(DateType.DATE_TIME);
		}
	}
	
	/**
	 * <p>Date, time and zone as "yyyy-MM-dd'T'HH:mm:ssZ".
	 * <p>Example: {@code '2001-07-04T12:08:56-0700'}.
	 */
	static final public class DateTimeZone extends AbstractDateTimeParser
	{
		private DateTimeZone()
		{
			super(DateType.DATE_TIME_ZONE);
		}
	}
	
	/**
	 * <p>Timestamp as "yyyy-MM-dd'T'HH:mm:ss.SSSXXX".
	 * <p>Example: {@code '1996-12-19T16:39:57.523-08:00'}.
	 */
	static final public class TimestampRFC_3339 extends AbstractDateTimeParser
	{
		private TimestampRFC_3339()
		{
			super(DateType.RFC_3339);
		}
	}
	
	/**
	 * <p>Timestamp as "EEE, dd MMM yyyy HH:mm:ss zz".
	 * <p>Example: {@code 'Sun, 06 Nov 1994 08:49:37 GMT'}.
	 */
	static final public class TimestampRFC_822_1123 extends AbstractDateTimeParser
	{
		private TimestampRFC_822_1123()
		{
			super(DateType.RFC_822_1123);
		}
	}
	
	/**
	 * <p>Timestamp as "EEEEE, dd-MMM-yy HH:mm:ss zz".
	 * <p>Example: {@code 'Sunday, 06-Nov-94 08:49:37 GMT'}.
	 */
	static final public class TimestampRFC_850_1036 extends AbstractDateTimeParser
	{
		private TimestampRFC_850_1036()
		{
			super(DateType.RFC_850_1036);
		}
	}
}
