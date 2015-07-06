package com.example.broadlinksdkdemo;

import java.util.ArrayList;

/**
 * Created by Torin on 6/7/15.
 */
public class JsonDevice {


	/* From Broadlink App
	  {
		"id": 1,
		"terminalId": 1,
		"mac": "b4430d10563a",
		"subDevice": 0,D
		"longitude": 0,
		"latitude": 0,
		"type": 10002,
		"new": 0,
		"password": 1658915694,
		"lock": 0,
		"publicKey": [
		  -76,
		  42,
		  -109,
		  36,
		  -21,
		  -99,
		  9,
		  20,
		  33,
		  17,
		  -128,
		  3,
		  88,
		  -124,
		  -10,
		  114
		],
		"name": "Thai An"
	  }
	 */

    public String mac;
    public String name;
    public String key;
	public int type;
    public int lock;
    public int password;
    public int id;
    public int terminalId;
    public int subDevice;

    public ArrayList<Integer> publicKey;
}
