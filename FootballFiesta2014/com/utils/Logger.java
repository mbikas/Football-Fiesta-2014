/*
 * Logger.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;


/**
 * 
 */
public class Logger 
{
    public Logger() {    }
    
    public void createPointTableSql()
    {
        int size = Utils.groupVector.size();
        String sql = "";
        for(int i=0;i<size;i++)
        {
            Group1 group = (Group1)Utils.groupVector.elementAt(i);
            for(int j=0;j<4;j++)
            {
                String teamId = group.teamNames[j];
                Team1 team = Utils.getTeamNameFromId(teamId);
                sql += "insert into point_table values( ";
                sql += "'"+group.groupName + "'," + "'"+teamId + "',0,0,0,0,0,0,0, '" +  team.name + "' );\n";
                System.out.println(teamId + " " + team.name);
            }
        }
        System.out.println(sql);
    }
    
    public void createMatchTableSql()
    {
        int size = Utils.matchVector.size();
        String sql = "";
        for(int i=0;i<size;i++)
        {
            Match1 match = (Match1)Utils.matchVector.elementAt(i);
            String team1Name = match.team1Id;
            String team2Name = match.team2Id;
            Team1 team1 = Utils.getTeamIdFromName(team1Name);
            Team1 team2 = Utils.getTeamIdFromName(team2Name);
            sql += "insert into matches values( ";
            sql += match.id + ",'"+team1.id + "','" + team2.id + "',0,0,'','','" + team1Name + "','"+ team2Name + "' );\n";
        }
        
        size = Utils.remainingmatchVector.size();
        for(int i=0;i<size;i++)
        {
            Match1 match = (Match1)Utils.remainingmatchVector.elementAt(i);
            String team1Name = match.team1Id;
            String team2Name = match.team2Id;
            sql += "insert into matches values( ";
            sql += match.id + ",'"+team1Name + "','" + team2Name + "',0,0,'','','" + team1Name + "','"+ team2Name + "' );\n";
        }
        System.out.println(sql);
    }
} 
