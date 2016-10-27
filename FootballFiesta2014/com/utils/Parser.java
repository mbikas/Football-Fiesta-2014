/*
 * Parser.java
 *
 */

package com.utils;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import java.util.Vector;

import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.util.Vector;
import java.io.*;
import net.rim.device.api.io.*;


/**
 *@author bikas
 *@date 01/01/2014
*/
public class Parser 
{
    public Parser() 
    {
                
    }
    
    public void parseMatchScoresFromFile()
    {
        InputStream is = null;
        try 
        {
            String line = null;
            is = this.getClass().getResourceAsStream("matchScores.txt");
            try {
                LineReader lr = new LineReader(is);
                line = new String(lr.readLine(), "UTF-8");
                Parser.parseMatchScoresResposne(line);
                if ( is != null)    is.close();
            }
            catch(EOFException eof){ 
                if ( is != null)    is.close();
            }
            
        } 
        catch (IOException e) 
        {   
            e.printStackTrace();
        } 
        finally 
        {
            //if ( is != null)    is.close();
        }    
    }
    
    public void parsePointTableFromFile()
    {
        InputStream is = null;
        try 
        {
            String line = null;
            is = this.getClass().getResourceAsStream("pointTable.txt");
            try {
                LineReader lr = new LineReader(is);
                line = new String(lr.readLine(), "UTF-8");
                Parser.parsePointTableResposne(line);
                if ( is != null)    is.close();
            }
            catch(EOFException eof){ 
                if ( is != null)    is.close();
            }
            
        } 
        catch (IOException e) 
        {   
            e.printStackTrace();
        } 
        finally 
        {
            //if ( is != null)    is.close();
        }    
    }
    
    public static void parsePointTableResposne(String output)
    {
        Utils.pointTableVector.removeAllElements();                                
        //A$BRA$Brazil$0$0$0$0$0$0$0@A$CRO$Croatia$0$0$0$0$0$0$0@B$ESP$Spain$0$0$0$0$0$0$0@
        String teams[] = Utils.split(output, "@");
        for(int i=0;i<teams.length;i++)
        {
            String team[] = Utils.split(teams[i], "$");
            if(team.length <=1) continue;
            PointTable pointTable = new PointTable();
            pointTable.groupName = team[0];
            pointTable.teamId = team[1];
            pointTable.teamName = team[2];
            pointTable.MP = Integer.parseInt(team[3]);
            pointTable.W = Integer.parseInt(team[4]);
            pointTable.D = Integer.parseInt(team[5]);
            pointTable.L = Integer.parseInt(team[6]);
            pointTable.GF = Integer.parseInt(team[7]);
            pointTable.GA = Integer.parseInt(team[8]);
            pointTable.Pts = Integer.parseInt(team[9]);
            Utils.pointTableVector.addElement(pointTable); 
        }
    }
        
    public static void parseMatchScoresResposne(String output)
    {
        System.out.println(output);
        //@49$1A$2B$@50$1C$2D$@51$1B$2A$@52$1D$2C$@53$1E$2F$@54$1G$2H$@55$1F$2E$@56$1H$2G$@57$W49$W50$@58$W53$W54$@59$W51$W52$@60$W55$W56$@61$W57$W58$@62$W59$W60$@63$L61$L62$@64$W61$W62$@
        String scores[] = Utils.split(output, "@");
        int size1 = Utils.matchVector.size();
        int size2 = Utils.remainingmatchVector.size();
        int length = scores.length;
        for(int i=0;i<length;i++)
        {
            String score[] = Utils.split(scores[i], "$");
            int len = score.length;
            if(len <=1) continue;
            int match_id = Integer.parseInt(score[0]);
            int team1_score = Integer.parseInt(score[1]);
            int team2_score = Integer.parseInt(score[2]);
            
            if(match_id < 49)
            {
                for(int j=0;j<size1;j++)
                {
                    Match1 mat = (Match1)Utils.matchVector.elementAt(j);
                    if(mat.id == match_id)
                    {
                        mat.team1_score = team1_score;
                        mat.team2_score = team2_score;
                        break;
                    }
                }
            }
            else
            {
                String team1_id = score[3];
                String team2_id = score[4];
     //           System.out.println(team1_id + " VS " + team2_id);
                //String extra = score[5];
                for(int j=0;j<size2;j++)
                {
                    Match1 mat = (Match1)Utils.remainingmatchVector.elementAt(j);
                    if(mat.id == match_id)
                    {
                        mat.team1_score = team1_score;
                        mat.team2_score = team2_score;
                        
                        mat.team1Id = team1_id;
                        mat.team2Id = team2_id;
                        
                        Team1 team1 = Utils.getTeamNameFromId(team1_id);
                        if(team1 != null)
                        {
                            mat.team1Name = team1.name;
                            mat.flag1 = team1.flagImageName;
                        }
                        else
                            mat.team1Name = team1_id;
                        Team1 team2 = Utils.getTeamNameFromId(team2_id);
                        if(team2 != null)
                        {
                            mat.team2Name = team2.name;
                            mat.flag2 = team2.flagImageName;
                        }
                        else
                            mat.team2Name = team2_id;
                        
                        if(len > 4)
                            mat.extra = score[5];
                        //mat.extra = extra;
                        break;
                    }
                }
            }
        }
        
    }
    
    public void parseSquad()
    {
        KXmlParser parser = null;
        Squad squad = null;
        Player player = null;
        Utils.squadVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("squad1.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("id") == true) 
                        {
                            if(squad != null)
                            {
                                Utils.squadVector.addElement(squad);
                            }
                            squad = new Squad();
                            squad.team_id = getStringValue(parser, eventType);
                        }
                        else if (elementName.equalsIgnoreCase("name") == true) 
                            squad.team_name = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("coach") == true) 
                            squad.coach = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("no") == true) 
                        {
                            player = new Player();
                            player.player_id = getStringValue(parser, eventType);
                        }
                        else if (elementName.equalsIgnoreCase("pos") == true) 
                            player.position = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("pname") == true) 
                            player.name = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("age") == true) 
                            player.age = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("caps") == true) 
                            player.caps = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("club") == true)
                        { 
                            player.club = getStringValue(parser, eventType);
                            squad.playerVector.addElement(player);    
                        }
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(squad != null)
                {
                    //squad.playerVector.addElement(player);
                    Utils.squadVector.addElement(squad);
                }
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
       
       
    public void parseGroups()
    {
        KXmlParser parser = null;
        Group1 group = null;
        int teamCount = 0;
        Utils.groupVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("groups.xml");  
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                   
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("name") == true) 
                        {
                            if(teamCount > 0)
                                Utils.groupVector.addElement(group);
                            group = new Group1();
                            teamCount = 0;
                            String groupName = "";
                            eventType = parser.next();
                            if (eventType == XmlPullParser.TEXT)
                            {
                                groupName = new String(parser.getText().getBytes("UTF-8")) ;
                            }
                            group.groupName = groupName;
                        }
                        else if (elementName.equalsIgnoreCase("team") == true) 
                        {
                            String teamName ="";
                            eventType = parser.next();
                            if (eventType == XmlPullParser.TEXT)
                            {
                                String str = parser.getText();
                                teamName = new String(parser.getText().trim().getBytes("UTF-8")) ;
                            }
                            group.teamNames[teamCount] = teamName;
                            teamCount++;
                        }
                    }                    
                } while (eventType != XmlPullParser.END_DOCUMENT);
                
                if(teamCount > 0)
                        Utils.groupVector.addElement(group);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    
    public void parseTeams()
    {
        KXmlParser parser = null;
        Team1 team = null;
        Utils.teamVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("teams.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("id") == true) 
                        {
                            if(team != null)
                                Utils.teamVector.addElement(team);
                            team = new Team1();
                            team.id = getStringValue(parser, eventType);
                            team.flagImageName = team.id + ".png";
                            team.flagImageNameSmall = team.id + "_small.png";
                        }
                        else if (elementName.equalsIgnoreCase("name") == true) 
                            team.name = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("nickname") == true) 
                            team.nickName = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("region") == true) 
                            team.region = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("rank") == true) 
                            team.rank = Integer.parseInt(getStringValue(parser, eventType));
                        else if (elementName.equalsIgnoreCase("coach") == true) 
                            team.coach = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("captain") == true) 
                            team.captain = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("appearances") == true) 
                            team.appearances = Integer.parseInt(getStringValue(parser, eventType));
                        else if (elementName.equalsIgnoreCase("best_results") == true) 
                            team.bestResults = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("key_player") == true)
                        { 
                            String keyPlayer = getStringValue(parser, eventType);
                            team.keyPlayers.addElement(keyPlayer);
                        }
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(team != null)
                        Utils.teamVector.addElement(team);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void parseCity()
    {
        KXmlParser parser = null;
        City1 city = null;
        Utils.cityVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("cities.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("id") == true) 
                        {
                            if(city != null)
                                Utils.cityVector.addElement(city);
                            city = new City1();
                            city.id = getStringValue(parser, eventType);
                        }
                        else if (elementName.equalsIgnoreCase("name") == true) 
                            city.name = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("stadium") == true) 
                            city.stadium = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("imageName") == true) 
                            city.imageName  = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("imageNameBig") == true) 
                            city.imageNameBig  = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("club") == true)
                            city.clubVector.addElement(getStringValue(parser, eventType));                            
                        else if (elementName.equalsIgnoreCase("history") == true) 
                            city.history = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("weather") == true) 
                            city.weather = getStringValue(parser, eventType);
                        
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(city != null)
                        Utils.cityVector.addElement(city);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void parseStadium()
    {
        KXmlParser parser = null;
        Stadium1 stadium = null;
        Utils.stadiumVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("stadiums.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("id") == true) 
                        {
                            if(stadium != null)
                                Utils.stadiumVector.addElement(stadium);
                            stadium = new Stadium1();
                            stadium.id = getStringValue(parser, eventType);
                        }
                        else if (elementName.equalsIgnoreCase("name") == true) 
                            stadium.name = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("imageName") == true) 
                            stadium.imageName  = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("imageNameBig") == true) 
                            stadium.imageNameBig = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("city") == true) 
                            stadium.city = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("year") == true) 
                            stadium.year = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("capacity") == true) 
                            stadium.capacity = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("description") == true)
                        { 
                            stadium.description = getStringValue(parser, eventType);                            
                        }
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(stadium != null)
                        Utils.stadiumVector.addElement(stadium);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void parseHistory()
    {
        KXmlParser parser = null;
        History1 history = null;
        Utils.historyVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("history.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("year") == true) 
                        {
                            if(history != null)
                                Utils.historyVector.addElement(history);
                            history = new History1();
                            history.year = getStringValue(parser, eventType);
                        }
                        else if (elementName.equalsIgnoreCase("host") == true) 
                            history.host = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("dates") == true) 
                            history.dates = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("teams") == true) 
                            history.teams = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("champion") == true) 
                            history.champion = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("runnerup") == true) 
                            history.runnerUp = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("third") == true) 
                            history.third = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("fourth") == true) 
                            history.fourth = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("player") == true)
                            history.bootVector.addElement(getStringValue(parser, eventType));                            
                        else if (elementName.equalsIgnoreCase("goldenball") == true) 
                            history.goldenBall = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("goalkeeper") == true) 
                            history.goalkeeper = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("youngPlayer") == true) 
                            history.youngPlayer = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("fairPlay") == true) 
                            history.fairPlay = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("finalresult") == true) 
                            history.finalResult = getStringValue(parser, eventType);
                        
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(history != null)
                        Utils.historyVector.addElement(history);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void parseMatch()
    {
        KXmlParser parser = null;
        Match1 match = null;
        Utils.matchVector.removeAllElements();
        try
        {
           
            InputStream inputStream  = this.getClass().getResourceAsStream("matches.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("id") == true) 
                        {
                            if(match != null)
                                Utils.matchVector.addElement(match);
                            match = new Match1();
                            match.id = Integer.parseInt(getStringValue(parser, eventType));
                        }
                        else if (elementName.equalsIgnoreCase("date") == true) 
                            match.date = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("time") == true) 
                            match.time = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("stadium") == true) 
                            match.stadium = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("team1") == true)
                        { 
                            String team1ID = getStringValue(parser, eventType);
                            match.team1Id = team1ID;
                            Team1 team = Utils.getTeamNameFromId(team1ID);
                            match.team1Name = team.name;
                            match.flag1 = team.flagImageName;//team.flagImageNameSmall;
                        }
                        else if (elementName.equalsIgnoreCase("team2") == true) 
                        {
                            String team2ID = getStringValue(parser, eventType);
                            match.team2Id = team2ID;
                            Team1 team = Utils.getTeamNameFromId(team2ID);
                            match.team2Name = team.name;
                            match.flag2 = team.flagImageName;//team.flagImageNameSmall;
                        }
                        else if (elementName.equalsIgnoreCase("group") == true) 
                            match.group = getStringValue(parser, eventType);
                        
                        
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(match != null)
                        Utils.matchVector.addElement(match);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public void parseRemainingMatch()
    {
        KXmlParser parser = null;
        Match1 match = null;
        Utils.remainingmatchVector.removeAllElements();
        try
        {
            
            InputStream inputStream  = this.getClass().getResourceAsStream("match_round2.xml");     
            parser = new KXmlParser();
            parser.setInput(inputStream, "UTF-8");  
        
            ///start Parsing//////////
            try 
            {   
                int eventType = XmlPullParser.END_DOCUMENT;
                String elementName = "";
                do 
                {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) 
                    {
                        elementName = parser.getName();
                        //System.out.println(elementName);
                        if (elementName.equalsIgnoreCase("id") == true) 
                        {
                            if(match != null)
                                Utils.remainingmatchVector.addElement(match);
                            match = new Match1();
                            match.id = Integer.parseInt(getStringValue(parser, eventType));
                        }
                        else if (elementName.equalsIgnoreCase("date") == true) 
                            match.date = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("time") == true) 
                            match.time = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("stadium") == true) 
                            match.stadium = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("team1") == true) 
                            match.team1Id = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("team2") == true) 
                            match.team2Id = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("group") == true) 
                            match.group = getStringValue(parser, eventType);
                        else if (elementName.equalsIgnoreCase("title") == true) 
                            match.title = getStringValue(parser, eventType);
                        
                        
                    }
                } while (eventType != XmlPullParser.END_DOCUMENT);
                if(match != null)
                        Utils.remainingmatchVector.addElement(match);
            } 
            catch (XmlPullParserException parserException) 
            {
                System.out.println("XmlPullParserException");
            } 
            catch (IOException ioException) 
            {
                System.out.println("IOException");
            }
            ///End Parsing////////////     
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    public static String getStringValue(KXmlParser parser, int eventType)
    {
        try
        {
            String value ="";
            eventType = parser.next();
            if (eventType == XmlPullParser.TEXT)
                value = parser.getText().trim();
            return value;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            return "";
        }
    }    
}

