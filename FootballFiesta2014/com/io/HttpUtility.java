/*
 * HttpUtility.java
 */

package com.io;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioInfo;

/*
@author Bikas
*/
public class HttpUtility
{   

        public static boolean isConnectionAvailable()
        {
                if (DeviceInfo.isSimulator())
                        return true; // ";deviceside=true;ConnectionTimeout=20000";
                if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != 0)
                {
                        try
                        {
                                if (CoverageInfo.isCoverageSufficient(1, RadioInfo.WAF_WLAN,
                                                false))
                                {
                                        return true;
                                }
                        }
                        catch (Exception ex)
                        {
                        }
                }
                if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT))
                        return true;
                if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS))
                        return true;
                /*
                 * else if ((CoverageInfo.getCoverageStatus() &
                 * CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B) return
                 * true; else if ((CoverageInfo.getCoverageStatus() &
                 * CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) return true;
                 * if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B))
                 * return true;
                 * if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT))
                 * return true;
                 * if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS))
                 * return true; if (WLANInfo.getWLANState() ==
                 * WLANInfo.WLAN_STATE_CONNECTED) return true;
                 */
                return false;
        }
        public static boolean USE_MDS_IN_SIMULATOR = true;
        public static String getConnectionString()
        {
                String st = "";
                if (DeviceInfo.isSimulator())
                {
                    if(HttpUtility.USE_MDS_IN_SIMULATOR)
                    {
                            //return ";deviceside=false;ConnectionTimeout=20000";
                            return ";deviceside=false";
                    }
                    else
                    {
                            //return ";deviceside=true;ConnectionTimeout=20000";
                            return ";deviceside=true";
                    }
                }
                if ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != 0)
                {
                        try
                        {
                                if (CoverageInfo.isCoverageSufficient(1, RadioInfo.WAF_WLAN,
                                                false))
                                {
                                        st = ";interface=wifi";
                                }
                        }
                        catch (Exception ex)
                        {
                        }
                }
                // A carrier is providing us with the data service
                else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B)
                {
                        // blackberry internet service
                        ServiceRecord rec = getBIBSRecord();
                        if (rec != null)// couldn't find the right record
                                st = ";deviceside=false;connectionUID=" + rec.getUid()
                                                + ";ConnectionType=mds-public";
                        else if (getwap2parameters() != null)
                                st = ";ConnectionUID=" + getwap2parameters();
                        else
                                // found the record, get the id
                                st = ";deviceside=true";// let the phone try to do the work
                }
                else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS)
                        st = ";deviceside=false";// use the clients blackberry enterprise
                // server
                else
                        st = ";deviceside=true";// let the phone do the work if it can

                return st;
                //return st + ";ConnectionTimeout=20000";
        }

        // gets the record referring to the BIS configuration
        public static ServiceRecord getBIBSRecord()
        {
                ServiceRecord[] ippps = ServiceBook.getSB().getRecords();
                for (int i = 0; i < ippps.length; i++)
                {
                        if (ippps[i].getCid().equals("IPPP"))
                        {
                                if (ippps[i].getName().indexOf("BIBS") >= 0)
                                        return ippps[i];
                        }
                }
                return null;
        }

        public static String getwap2parameters()
        {
                ServiceBook sb = ServiceBook.getSB();
                ServiceRecord[] records = sb.findRecordsByCid("WPTCP");
                String uid = null;

                for (int i = 0; i < records.length; i++)
                {
                        // Search through all service records to find the
                        // valid non-Wi-Fi and non-MMS
                        // WAP 2.0 Gateway Service Record.
                        if (records[i].isValid() && !records[i].isDisabled())
                        {
                                if (records[i].getUid() != null
                                                && records[i].getUid().length() != 0)
                                {
                                        if ((records[i].getUid().toLowerCase().indexOf("wifi") == -1)
                                                        && (records[i].getUid().toLowerCase()
                                                                        .indexOf("mms") == -1))
                                        {
                                                uid = records[i].getUid();
                                                break;
                                        }
                                }
                        }
                }
                return uid;
        }

        // public static Vector getContacts() {
        // Vector result = new Vector();
        // String[] contact;
        // int numberCount = 0;
        // try {
        // BlackBerryContactList contactList = (BlackBerryContactList) PIM
        // .getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY);
        // Enumeration enumx = contactList
        // .items(BlackBerryContactList.SEARCH_GROUPS
        // | BlackBerryContactList.SEARCH_CONTACTS);
        // while (enumx.hasMoreElements()) {
        // BlackBerryContact c = (BlackBerryContact) enumx.nextElement();
        // numberCount = 0;
        // if (contactList.isSupportedField(BlackBerryContact.NAME)) {
        // numberCount++;
        // }
        //
        // if (contactList.isSupportedField(BlackBerryContact.TEL)) {
        //
        // int count = c.countValues(BlackBerryContact.TEL);
        // Utility.showAlertDialog("count: " + count);
        // numberCount += count;
        // }
        //
        // contact = new String[numberCount];
        // Utility.showAlertDialog("numberCount: " + numberCount);
        //
        // if (contactList.isSupportedField(BlackBerryContact.NAME)) {
        // String[] name = c.getStringArray(BlackBerryContact.NAME, 0);
        // String firstName = "", lastName = "";
        // firstName = name[Contact.NAME_GIVEN];
        // lastName = name[Contact.NAME_FAMILY];
        // if (firstName == null) {
        // firstName = "";
        // }
        // if (lastName == null) {
        // lastName = "";
        // }
        // contact[0] = firstName + " " + lastName;
        // Utility.showAlertDialog(contact[0]);
        // }
        //
        // if (contactList.isSupportedField(BlackBerryContact.TEL)) {
        //
        // for (int i = 0; i < numberCount; i++) {
        // contact[i] = c.getString(BlackBerryContact.TEL, i);
        // Utility.showAlertDialog(contact[i]);
        // }
        //
        // }
        //
        // result.addElement(contact);
        // }
        // } catch (Exception ex) {
        // Utility.showAlertDialog("Exception: " + ex.getMessage());
        // ex.printStackTrace();
        // }
        // return result;
        // }

}
