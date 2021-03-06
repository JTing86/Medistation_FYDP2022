EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 2 3
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Connector:USB_B_Micro J17
U 1 1 616251CA
P 1250 1800
F 0 "J17" H 1307 2267 50  0000 C CNN
F 1 "USB_B_Micro" H 1307 2176 50  0000 C CNN
F 2 "microUSB:AMPHENOL_10118194-0001LF" H 1400 1750 50  0001 C CNN
F 3 "~" H 1400 1750 50  0001 C CNN
	1    1250 1800
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR033
U 1 1 6162612A
P 1250 2350
F 0 "#PWR033" H 1250 2100 50  0001 C CNN
F 1 "GND" H 1255 2177 50  0000 C CNN
F 2 "" H 1250 2350 50  0001 C CNN
F 3 "" H 1250 2350 50  0001 C CNN
	1    1250 2350
	1    0    0    -1  
$EndComp
Wire Wire Line
	1250 2350 1250 2300
Wire Wire Line
	1150 2200 1150 2300
Wire Wire Line
	1150 2300 1250 2300
Connection ~ 1250 2300
Wire Wire Line
	1250 2300 1250 2250
$Comp
L Device:D_Zener D1
U 1 1 61626536
P 2050 1850
F 0 "D1" V 2004 1930 50  0000 L CNN
F 1 "SMAJ5.0A" V 2095 1930 50  0000 L CNN
F 2 "TLV_DIODE:SMAJ5.0A" H 2050 1850 50  0001 C CNN
F 3 "~" H 2050 1850 50  0001 C CNN
	1    2050 1850
	0    1    1    0   
$EndComp
Wire Wire Line
	2050 1600 2050 1700
$Comp
L power:GND #PWR035
U 1 1 61626AAC
P 2050 2050
F 0 "#PWR035" H 2050 1800 50  0001 C CNN
F 1 "GND" H 2055 1877 50  0000 C CNN
F 2 "" H 2050 2050 50  0001 C CNN
F 3 "" H 2050 2050 50  0001 C CNN
	1    2050 2050
	1    0    0    -1  
$EndComp
Wire Wire Line
	2050 2000 2050 2050
$Comp
L Regulator_Linear:uA7805 U5
U 1 1 6162735A
P 3750 1450
F 0 "U5" H 3750 1692 50  0000 C CNN
F 1 "uA7805" H 3750 1601 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:TO-263-3_TabPin2" H 3775 1300 50  0001 L CIN
F 3 "http://www.ti.com/lit/ds/symlink/ua78.pdf" H 3750 1400 50  0001 C CNN
	1    3750 1450
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR040
U 1 1 61627A81
P 4200 1350
F 0 "#PWR040" H 4200 1200 50  0001 C CNN
F 1 "+5V" H 4215 1523 50  0000 C CNN
F 2 "" H 4200 1350 50  0001 C CNN
F 3 "" H 4200 1350 50  0001 C CNN
	1    4200 1350
	1    0    0    -1  
$EndComp
$Comp
L Device:CP1_Small C14
U 1 1 61628493
P 4200 1600
F 0 "C14" H 4291 1646 50  0000 L CNN
F 1 "0.1 uF" H 4291 1555 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 4200 1600 50  0001 C CNN
F 3 "~" H 4200 1600 50  0001 C CNN
	1    4200 1600
	1    0    0    -1  
$EndComp
$Comp
L Device:CP1_Small C13
U 1 1 61628571
P 3300 1600
F 0 "C13" H 3391 1646 50  0000 L CNN
F 1 "0.33 uF" H 3400 1550 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 3300 1600 50  0001 C CNN
F 3 "~" H 3300 1600 50  0001 C CNN
	1    3300 1600
	-1   0    0    -1  
$EndComp
$Comp
L power:GND #PWR039
U 1 1 61628C2C
P 3750 1900
F 0 "#PWR039" H 3750 1650 50  0001 C CNN
F 1 "GND" H 3755 1727 50  0000 C CNN
F 2 "" H 3750 1900 50  0001 C CNN
F 3 "" H 3750 1900 50  0001 C CNN
	1    3750 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	3750 1900 3750 1850
Wire Wire Line
	4200 1700 4200 1800
Wire Wire Line
	4200 1800 3750 1800
Connection ~ 3750 1800
Wire Wire Line
	3750 1800 3750 1750
Wire Wire Line
	3300 1700 3300 1850
Wire Wire Line
	3300 1850 3750 1850
Connection ~ 3750 1850
Wire Wire Line
	3750 1850 3750 1800
Wire Wire Line
	4200 1350 4200 1400
Wire Wire Line
	4050 1450 4200 1450
Connection ~ 4200 1450
Wire Wire Line
	4200 1450 4200 1500
Wire Wire Line
	3450 1450 3300 1450
Wire Wire Line
	3300 1450 3300 1500
Text GLabel 3200 1450 0    50   Input ~ 0
POWER
Wire Wire Line
	3200 1450 3300 1450
Connection ~ 3300 1450
Text Notes 1200 1100 0    50   ~ 0
micro USB for power from wall plug\n
Text Notes 3600 1000 0    50   ~ 0
5V LDO\n
$Comp
L Device:Battery_Cell BT1
U 1 1 6162B4AB
P 8200 1850
F 0 "BT1" H 8318 1946 50  0000 L CNN
F 1 "AA_holder" H 8318 1855 50  0000 L CNN
F 2 "battery_holder:battery_holder" V 8200 1910 50  0001 C CNN
F 3 "~" V 8200 1910 50  0001 C CNN
	1    8200 1850
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR045
U 1 1 6162BAE8
P 8200 2200
F 0 "#PWR045" H 8200 1950 50  0001 C CNN
F 1 "GND" H 8205 2027 50  0000 C CNN
F 2 "" H 8200 2200 50  0001 C CNN
F 3 "" H 8200 2200 50  0001 C CNN
	1    8200 2200
	1    0    0    -1  
$EndComp
Wire Wire Line
	8200 2200 8200 2050
$Comp
L power:+BATT #PWR044
U 1 1 6162C4F1
P 8200 1050
F 0 "#PWR044" H 8200 900 50  0001 C CNN
F 1 "+BATT" H 8215 1223 50  0000 C CNN
F 2 "" H 8200 1050 50  0001 C CNN
F 3 "" H 8200 1050 50  0001 C CNN
	1    8200 1050
	1    0    0    -1  
$EndComp
Wire Wire Line
	8200 1050 8200 1150
$Comp
L Device:CP1_Small C16
U 1 1 6162CD31
P 8850 1300
F 0 "C16" V 9078 1300 50  0000 C CNN
F 1 "10 uF" V 8987 1300 50  0000 C CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 8850 1300 50  0001 C CNN
F 3 "~" H 8850 1300 50  0001 C CNN
	1    8850 1300
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8750 1300 8500 1300
$Comp
L power:GND #PWR046
U 1 1 6162DFB9
P 9050 1350
F 0 "#PWR046" H 9050 1100 50  0001 C CNN
F 1 "GND" H 9055 1177 50  0000 C CNN
F 2 "" H 9050 1350 50  0001 C CNN
F 3 "" H 9050 1350 50  0001 C CNN
	1    9050 1350
	1    0    0    -1  
$EndComp
Wire Wire Line
	8950 1300 9050 1300
Wire Wire Line
	9050 1300 9050 1350
$Comp
L LDO:BU33SD5WG U6
U 1 1 6163D83B
P 5950 1600
F 0 "U6" H 5950 2025 50  0000 C CNN
F 1 "BU33SD5WG" H 5950 1934 50  0000 C CNN
F 2 "SSOP5:BU33SD5WG-TR" H 5950 1950 50  0001 C CNN
F 3 "" H 5950 1950 50  0001 C CNN
	1    5950 1600
	1    0    0    -1  
$EndComp
Text GLabel 5350 1400 0    50   Input ~ 0
POWER
Wire Wire Line
	5350 1400 5400 1400
Wire Wire Line
	5400 1400 5400 1600
Wire Wire Line
	5400 1600 5500 1600
Connection ~ 5400 1400
Wire Wire Line
	5400 1400 5500 1400
$Comp
L power:GND #PWR041
U 1 1 6165C4DA
P 5400 1850
F 0 "#PWR041" H 5400 1600 50  0001 C CNN
F 1 "GND" H 5405 1677 50  0000 C CNN
F 2 "" H 5400 1850 50  0001 C CNN
F 3 "" H 5400 1850 50  0001 C CNN
	1    5400 1850
	1    0    0    -1  
$EndComp
Wire Wire Line
	5400 1850 5400 1800
Wire Wire Line
	5400 1800 5500 1800
$Comp
L Device:CP1_Small C15
U 1 1 6165EBC7
P 6550 1750
F 0 "C15" H 6641 1796 50  0000 L CNN
F 1 "10uF" H 6641 1705 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 6550 1750 50  0001 C CNN
F 3 "~" H 6550 1750 50  0001 C CNN
	1    6550 1750
	1    0    0    -1  
$EndComp
Wire Wire Line
	6400 1600 6500 1600
Wire Wire Line
	6550 1600 6550 1650
$Comp
L power:GND #PWR043
U 1 1 61660AC2
P 6550 1900
F 0 "#PWR043" H 6550 1650 50  0001 C CNN
F 1 "GND" H 6555 1727 50  0000 C CNN
F 2 "" H 6550 1900 50  0001 C CNN
F 3 "" H 6550 1900 50  0001 C CNN
	1    6550 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	6550 1900 6550 1850
$Comp
L power:+3.3V #PWR042
U 1 1 61662BEB
P 6500 1400
F 0 "#PWR042" H 6500 1250 50  0001 C CNN
F 1 "+3.3V" H 6515 1573 50  0000 C CNN
F 2 "" H 6500 1400 50  0001 C CNN
F 3 "" H 6500 1400 50  0001 C CNN
	1    6500 1400
	1    0    0    -1  
$EndComp
Wire Wire Line
	6500 1400 6500 1500
Connection ~ 6500 1600
Wire Wire Line
	6500 1600 6550 1600
Text Notes 5800 1000 0    50   ~ 0
3.3V LDO\n
Text Notes 8100 750  0    50   ~ 0
AA bettery holder
Wire Wire Line
	5300 3600 5300 3700
$Comp
L power:PWR_FLAG #FLG02
U 1 1 616BC038
P 5300 3600
F 0 "#FLG02" H 5300 3675 50  0001 C CNN
F 1 "PWR_FLAG" H 5300 3773 50  0000 C CNN
F 2 "" H 5300 3600 50  0001 C CNN
F 3 "~" H 5300 3600 50  0001 C CNN
	1    5300 3600
	1    0    0    -1  
$EndComp
Text Notes 1900 3250 0    50   ~ 0
Only use battery if no plug power and battery has sufficient charge
Text GLabel 5450 3700 2    50   Output ~ 0
POWER
Wire Wire Line
	4350 4100 4350 4050
Wire Wire Line
	3950 4100 4000 4100
Wire Wire Line
	4000 4100 4350 4100
Connection ~ 4000 4100
Wire Wire Line
	4000 4000 4000 4100
Wire Wire Line
	4350 3700 4350 3750
Connection ~ 4350 3700
Wire Wire Line
	4200 3700 4350 3700
$Comp
L Device:D D2
U 1 1 6164DA02
P 4350 3900
F 0 "D2" V 4304 3980 50  0000 L CNN
F 1 "D" V 4395 3980 50  0000 L CNN
F 2 "Diode_THT:D_DO-35_SOD27_P7.62mm_Horizontal" H 4350 3900 50  0001 C CNN
F 3 "~" H 4350 3900 50  0001 C CNN
	1    4350 3900
	0    1    1    0   
$EndComp
Wire Wire Line
	3600 3700 3800 3700
Wire Wire Line
	3000 3700 3200 3700
$Comp
L power:+BATT #PWR037
U 1 1 61650A6B
P 3000 3600
F 0 "#PWR037" H 3000 3450 50  0001 C CNN
F 1 "+BATT" H 3015 3773 50  0000 C CNN
F 2 "" H 3000 3600 50  0001 C CNN
F 3 "" H 3000 3600 50  0001 C CNN
	1    3000 3600
	1    0    0    -1  
$EndComp
Wire Wire Line
	3000 3600 3000 3700
$Comp
L voltage_sensor:S-1009N34I-M5T1U U4
U 1 1 6163BE5E
P 2550 4650
F 0 "U4" H 2575 5015 50  0000 C CNN
F 1 "S-1009N34I-M5T1U" H 2575 4924 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23-5_HandSoldering" H 2400 4750 50  0001 C CNN
F 3 "" H 2400 4750 50  0001 C CNN
	1    2550 4650
	1    0    0    -1  
$EndComp
$Comp
L power:+BATT #PWR034
U 1 1 6163EE86
P 2100 4400
F 0 "#PWR034" H 2100 4250 50  0001 C CNN
F 1 "+BATT" H 2115 4573 50  0000 C CNN
F 2 "" H 2100 4400 50  0001 C CNN
F 3 "" H 2100 4400 50  0001 C CNN
	1    2100 4400
	1    0    0    -1  
$EndComp
Wire Wire Line
	2100 4400 2100 4500
Connection ~ 2100 4500
Wire Wire Line
	2100 4550 2250 4550
$Comp
L Device:R_Small R5
U 1 1 61640470
P 1850 4650
F 0 "R5" H 1909 4696 50  0000 L CNN
F 1 "100k" H 1909 4605 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 1850 4650 50  0001 C CNN
F 3 "~" H 1850 4650 50  0001 C CNN
	1    1850 4650
	1    0    0    -1  
$EndComp
Wire Wire Line
	1850 4550 1850 4500
Wire Wire Line
	1850 4500 2100 4500
Wire Wire Line
	2100 4500 2100 4550
Wire Wire Line
	1850 4750 1850 4800
Wire Wire Line
	1850 4800 2250 4800
$Comp
L Device:C_Small C12
U 1 1 61641FC1
P 2950 4950
F 0 "C12" H 3042 4996 50  0000 L CNN
F 1 "1.8 nF" H 3042 4905 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 2950 4950 50  0001 C CNN
F 3 "~" H 2950 4950 50  0001 C CNN
	1    2950 4950
	1    0    0    -1  
$EndComp
Wire Wire Line
	2900 4800 2950 4800
Wire Wire Line
	2950 4800 2950 4850
$Comp
L power:GND #PWR036
U 1 1 61643564
P 2950 5100
F 0 "#PWR036" H 2950 4850 50  0001 C CNN
F 1 "GND" H 2955 4927 50  0000 C CNN
F 2 "" H 2950 5100 50  0001 C CNN
F 3 "" H 2950 5100 50  0001 C CNN
	1    2950 5100
	1    0    0    -1  
$EndComp
Wire Wire Line
	2950 5100 2950 5050
Wire Wire Line
	2900 4550 3100 4550
$Comp
L power:GND #PWR038
U 1 1 616465B6
P 3400 4850
F 0 "#PWR038" H 3400 4600 50  0001 C CNN
F 1 "GND" H 3405 4677 50  0000 C CNN
F 2 "" H 3400 4850 50  0001 C CNN
F 3 "" H 3400 4850 50  0001 C CNN
	1    3400 4850
	1    0    0    -1  
$EndComp
Wire Wire Line
	3400 4850 3400 4750
Wire Wire Line
	3400 4350 3400 4150
Connection ~ 3400 4150
Wire Wire Line
	3000 4100 3000 4150
Wire Wire Line
	3000 4150 3400 4150
Wire Wire Line
	3400 4150 3400 4000
Connection ~ 3000 3700
Wire Wire Line
	3000 3900 3000 3700
$Comp
L Device:R_Small R6
U 1 1 6164898D
P 3000 4000
F 0 "R6" H 3059 4046 50  0000 L CNN
F 1 "100K" H 3059 3955 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 3000 4000 50  0001 C CNN
F 3 "~" H 3000 4000 50  0001 C CNN
	1    3000 4000
	1    0    0    -1  
$EndComp
Text Notes 1900 4050 0    50   ~ 0
3.4V voltage detector
$Comp
L power:PWR_FLAG #FLG03
U 1 1 61629F1B
P 8500 1300
F 0 "#FLG03" H 8500 1375 50  0001 C CNN
F 1 "PWR_FLAG" H 8500 1473 50  0000 C CNN
F 2 "" H 8500 1300 50  0001 C CNN
F 3 "~" H 8500 1300 50  0001 C CNN
	1    8500 1300
	1    0    0    -1  
$EndComp
Connection ~ 8500 1300
Wire Wire Line
	8500 1300 8200 1300
$Comp
L Device:Q_PMOS_GSD Q3
U 1 1 6163EAA4
P 4000 3800
F 0 "Q3" V 4342 3800 50  0000 C CNN
F 1 "Q_PMOS_GSD" V 4251 3800 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23" H 4200 3900 50  0001 C CNN
F 3 "~" H 4000 3800 50  0001 C CNN
	1    4000 3800
	0    1    -1   0   
$EndComp
$Comp
L Device:Q_PMOS_GSD Q2
U 1 1 6163F4F3
P 3400 3800
F 0 "Q2" V 3742 3800 50  0000 C CNN
F 1 "Q_PMOS_GSD" V 3651 3800 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23" H 3600 3900 50  0001 C CNN
F 3 "~" H 3400 3800 50  0001 C CNN
	1    3400 3800
	0    1    -1   0   
$EndComp
$Comp
L Device:Q_NMOS_GSD Q1
U 1 1 616406CE
P 3300 4550
F 0 "Q1" H 3504 4596 50  0000 L CNN
F 1 "Q_NMOS_GSD" H 3504 4505 50  0000 L CNN
F 2 "Package_TO_SOT_SMD:SOT-23" H 3500 4650 50  0001 C CNN
F 3 "~" H 3300 4550 50  0001 C CNN
	1    3300 4550
	1    0    0    -1  
$EndComp
Text HLabel 1650 1800 2    50   Output ~ 0
D+
Text HLabel 1650 1900 2    50   Output ~ 0
D-
Wire Wire Line
	1650 1800 1550 1800
Wire Wire Line
	1650 1900 1550 1900
Wire Wire Line
	1550 2000 1600 2000
Wire Wire Line
	1600 2000 1600 2250
Wire Wire Line
	1600 2250 1250 2250
Connection ~ 1250 2250
Wire Wire Line
	1250 2250 1250 2200
Text HLabel 2300 1600 2    50   Output ~ 0
VBUS
Text HLabel 3950 4100 0    50   Input ~ 0
VBUS
$Comp
L Device:Fuse_Small F1
U 1 1 61E86CE9
P 1800 1600
F 0 "F1" H 1800 1785 50  0000 C CNN
F 1 "1.5A fuse" H 1800 1694 50  0000 C CNN
F 2 "Fuse:Fuse_0603_1608Metric_Pad1.05x0.95mm_HandSolder" H 1800 1600 50  0001 C CNN
F 3 "~" H 1800 1600 50  0001 C CNN
	1    1800 1600
	1    0    0    -1  
$EndComp
Wire Wire Line
	1900 1600 2050 1600
Connection ~ 2050 1600
Wire Wire Line
	1700 1600 1550 1600
Wire Wire Line
	2050 1600 2300 1600
$Comp
L Device:Fuse_Small F2
U 1 1 61E95CAA
P 8200 1450
F 0 "F2" V 8154 1498 50  0000 L CNN
F 1 "1.5A fuse" V 8245 1498 50  0000 L CNN
F 2 "Fuse:Fuse_0603_1608Metric_Pad1.05x0.95mm_HandSolder" H 8200 1450 50  0001 C CNN
F 3 "~" H 8200 1450 50  0001 C CNN
	1    8200 1450
	0    1    1    0   
$EndComp
Wire Wire Line
	8200 1350 8200 1300
Connection ~ 8200 1300
Wire Wire Line
	8200 1550 8200 1650
$Comp
L Switch:SW_SPST SW3
U 1 1 61EA20F9
P 4850 3700
F 0 "SW3" H 4850 3935 50  0000 C CNN
F 1 "PWR_SWITCH" H 4850 3844 50  0000 C CNN
F 2 "toggle:M2011S2A1W01" H 4850 3700 50  0001 C CNN
F 3 "~" H 4850 3700 50  0001 C CNN
	1    4850 3700
	1    0    0    -1  
$EndComp
Connection ~ 5300 3700
Wire Wire Line
	5300 3700 5450 3700
Wire Wire Line
	4350 3700 4650 3700
$Comp
L Connector:TestPoint TP5
U 1 1 620267A6
P 8000 1150
F 0 "TP5" V 8195 1222 50  0000 C CNN
F 1 "TestPoint" V 8104 1222 50  0000 C CNN
F 2 "TestPoint:TestPoint_Plated_Hole_D2.0mm" H 8200 1150 50  0001 C CNN
F 3 "~" H 8200 1150 50  0001 C CNN
	1    8000 1150
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8000 1150 8200 1150
Connection ~ 8200 1150
Wire Wire Line
	8200 1150 8200 1300
$Comp
L Connector:TestPoint TP4
U 1 1 6202BA02
P 7950 2050
F 0 "TP4" V 8145 2122 50  0000 C CNN
F 1 "TestPoint" V 8054 2122 50  0000 C CNN
F 2 "TestPoint:TestPoint_Plated_Hole_D2.0mm" H 8150 2050 50  0001 C CNN
F 3 "~" H 8150 2050 50  0001 C CNN
	1    7950 2050
	0    -1   -1   0   
$EndComp
Wire Wire Line
	7950 2050 8200 2050
Connection ~ 8200 2050
Wire Wire Line
	8200 2050 8200 1950
$Comp
L Connector:TestPoint TP3
U 1 1 62030A8E
P 6650 1500
F 0 "TP3" V 6604 1688 50  0000 L CNN
F 1 "TestPoint" V 6695 1688 50  0000 L CNN
F 2 "TestPoint:TestPoint_Plated_Hole_D2.0mm" H 6850 1500 50  0001 C CNN
F 3 "~" H 6850 1500 50  0001 C CNN
	1    6650 1500
	0    1    1    0   
$EndComp
Wire Wire Line
	6650 1500 6500 1500
Connection ~ 6500 1500
Wire Wire Line
	6500 1500 6500 1600
$Comp
L Connector:TestPoint TP2
U 1 1 620336EC
P 5200 3900
F 0 "TP2" H 5142 3926 50  0000 R CNN
F 1 "TestPoint" H 5142 4017 50  0000 R CNN
F 2 "TestPoint:TestPoint_Plated_Hole_D2.0mm" H 5400 3900 50  0001 C CNN
F 3 "~" H 5400 3900 50  0001 C CNN
	1    5200 3900
	-1   0    0    1   
$EndComp
Wire Wire Line
	5200 3900 5200 3700
Wire Wire Line
	5050 3700 5200 3700
Connection ~ 5200 3700
Wire Wire Line
	5200 3700 5300 3700
$Comp
L Connector:TestPoint TP1
U 1 1 620369C3
P 4300 1400
F 0 "TP1" V 4254 1588 50  0000 L CNN
F 1 "TestPoint" V 4345 1588 50  0000 L CNN
F 2 "TestPoint:TestPoint_Plated_Hole_D2.0mm" H 4500 1400 50  0001 C CNN
F 3 "~" H 4500 1400 50  0001 C CNN
	1    4300 1400
	0    1    1    0   
$EndComp
Wire Wire Line
	4300 1400 4200 1400
Connection ~ 4200 1400
Wire Wire Line
	4200 1400 4200 1450
$EndSCHEMATC
