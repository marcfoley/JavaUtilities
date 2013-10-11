package com.foley.marc.log;


import java.util.ArrayList;
import java.util.List;


/**
 * Match represents ...
 * 
 * @author <a href="mailto:mjfoley@24hourfit.com">mjfoley</a>
 * @version $Id$
 * @since May 15, 2012
 * 
 * @todo Complete description
 */
public class Match
{

   private String              m_pattern;
   private final List <String> m_matches = new ArrayList <String> ();


   public Match (final String pattern)
   {
      m_pattern = pattern;
   }


   /**
    * @return the pattern
    */
   public String getPattern ()
   {
      return m_pattern;
   }


   /**
    * @param pattern the pattern to set
    */
   public void setPattern (final String pattern)
   {
      m_pattern = pattern;
   }


   /**
    * @return the matches
    */
   public int getNumberOfMatches ()
   {
      return m_matches.size ();
   }


   public void addMatch (final String logLine)
   {
      m_matches.add (logLine);
   }
}
