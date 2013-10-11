package com.foley.marc.log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;


/**
 * LogParsing represents ...
 * 
 * @author <a href="mailto:mjfoley@24hourfit.com">mjfoley</a>
 * @version $Id$
 * @since May 15, 2012
 * 
 * @todo Complete description
 */
public class LogParsing
{

   private static final int              CONNECTION_TIMEOUT = 10000;
   private final static transient Logger m_log              = Logger.getLogger (LogParsing.class);


   /**
    * Represents main
    * 
    * @param args
    * @since May 15, 2012
    * 
    * @todo complete description
    */
   public static void main (final String[] args) throws Exception
   {
      final List <URL> _baseURLS = new ArrayList <URL> ();
		_baseURLS.add(new URL("http://www.google.com"));


      final int _daysBack = 10;
      final int _recentDaysToSkip = -1;
      final SimpleDateFormat _sdf = new SimpleDateFormat ("yyyy-MM-dd");

      final List <URL> _logURLs = new ArrayList <URL> ();
      for (final URL _url : _baseURLS)
      {
         for (int i = 0; i < _daysBack; i++)
         {
            if (i > _recentDaysToSkip)
            {
               final Calendar _logDate = Calendar.getInstance ();
               _logDate.add (Calendar.DATE, i * -1);
               m_log.debug (_sdf.format (_logDate.getTime ()));
               _logURLs.addAll (getLogURLs (_url, "server", _sdf.format (_logDate.getTime ())));
            }
         }
      }


      final List <String> _matches = new ArrayList <String> ();
      _matches.add ("JMSR");
      _matches.add ("JMSP");


      final Map <URL, List <Match>> _results = parseLog (_logURLs, _matches);
      logResults (_results);
   }


   /**
    * Represents logResults
    * 
    * @param _results
    * @since May 16, 2012
    * 
    * @todo complete description
    */
   private static void logResults (final Map <URL, List <Match>> _results)
   {
      m_log.info ("Log Files Parsed: " + _results.size ());
      for (final Entry <URL, List <Match>> _entry : _results.entrySet ())
      {
         m_log.info ("\tURL: " + _entry.getKey ().toString ());
         for (final Match _match : _entry.getValue ())
         {
            m_log.info ("\t\tFound " + _match.getNumberOfMatches () + " occurences for " + _match.getPattern ());
         }
      }
   }


   private static Collection <URL> getLogURLs (final URL baseURL, final String logFileName, final String logDate)
   {
      final Set <URL> _validURLs = new HashSet <URL> ();
      m_log.debug ("going to main page");
      try
      {
         final URLConnection _openConnection = baseURL.openConnection ();
         _openConnection.setConnectTimeout (CONNECTION_TIMEOUT);// 10 second
         _openConnection.setReadTimeout (CONNECTION_TIMEOUT);
         final Parser _parser = new Parser (_openConnection);
         final NodeFilter _filter = new NodeClassFilter (LinkTag.class);
         final NodeList _nodes = _parser.extractAllNodesThatMatch (_filter);

         Node _node;
         LinkTag _linkTag;
         for (int i = 0; i < _nodes.size (); i++)
         {
            _node = _nodes.elementAt (i);
            if (_node instanceof LinkTag)
            {
               _linkTag = (LinkTag) _node;
               m_log.debug ("\tpossible URL: " + _linkTag.getLinkText ());

               if (_linkTag.getLinkText ().contains (logFileName) && _linkTag.getLinkText ().contains (logDate))
               {
                  m_log.info (_linkTag.getLink ());
                  _validURLs.add (new URL (_linkTag.getLink ()));
               }


            }
         }
      }
      catch (final Exception e)
      {
         final String _msg = "Unable to get to log page";
         m_log.error (_msg, e);
         // throw new IllegalStateException (msg);
      }

      return _validURLs;
   }


   private static Map <URL, List <Match>> parseLog (final List <URL> logFiles, final List <String> patterns)
         throws IOException
   {
      final Map <URL, List <Match>> _results = new HashMap <URL, List <Match>> ();
      for (final URL _logFile : logFiles)
      {

         final URLConnection _urlConnection = _logFile.openConnection ();
         _urlConnection.setReadTimeout (CONNECTION_TIMEOUT);
         _urlConnection.setConnectTimeout (CONNECTION_TIMEOUT);
         InputStreamReader _inputReader;

         if (_logFile.toString ().endsWith ("gz"))
         {
            m_log.debug ("zipped file: " + _logFile.toExternalForm ());
            final GZIPInputStream _gzIS = new GZIPInputStream (_urlConnection.getInputStream ());
            _inputReader = new InputStreamReader (_gzIS);
         }
         else
         {
            _inputReader = new InputStreamReader (_urlConnection.getInputStream ());

         }

         final BufferedReader _input = new BufferedReader (_inputReader);
         String _inputLine;

         final List <Match> _matchers = createMatchers (patterns);
         while (true)
         {
            _inputLine = _input.readLine ();
            if (null == _inputLine)
            {
               break;
            }
            for (final Match _match : _matchers)
            {
               if (_inputLine.contains (_match.getPattern ()))
               {
                  m_log.info ("found match: " + _inputLine);
                  _match.addMatch (_inputLine);
               }
            }
         }
         _results.put (_logFile, _matchers);
      }
      return _results;
   }


   private static List <Match> createMatchers (final List <String> patterns)
   {
      final List <Match> _matchers = new ArrayList <Match> ();
      for (final String _pattern : patterns)
      {
         _matchers.add (new Match (_pattern));
      }
      return _matchers;
   }
}
