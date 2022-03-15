EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 3 4
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
L Battery_Management:MCP73831-2-OT U6
U 1 1 614703EF
P 3600 2250
F 0 "U6" H 3050 2700 50  0000 C CNN
F 1 "MCP73831-2-OT" H 3050 2600 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23-5_HandSoldering" H 3650 2000 50  0001 L CIN
F 3 "http://ww1.microchip.com/downloads/en/DeviceDoc/20001984g.pdf" H 3450 2200 50  0001 C CNN
	1    3600 2250
	1    0    0    -1  
$EndComp
$Comp
L Device:D_Zener D1
U 1 1 61471EB2
P 3900 1400
F 0 "D1" H 3900 1617 50  0000 C CNN
F 1 "SMAJ5.0A" H 3900 1526 50  0000 C CNN
F 2 "TLV_DIODE:SMAJ5.0A" H 3900 1400 50  0001 C CNN
F 3 "~" H 3900 1400 50  0001 C CNN
	1    3900 1400
	1    0    0    -1  
$EndComp
Wire Wire Line
	3750 1400 3600 1400
Connection ~ 3600 1400
Wire Wire Line
	3600 1400 3600 1950
Wire Wire Line
	4100 1450 4100 1400
Wire Wire Line
	4100 1400 4050 1400
$Comp
L power:+BATT #PWR0119
U 1 1 61472F28
P 4100 2050
F 0 "#PWR0119" H 4100 1900 50  0001 C CNN
F 1 "+BATT" H 4115 2223 50  0000 C CNN
F 2 "" H 4100 2050 50  0001 C CNN
F 3 "" H 4100 2050 50  0001 C CNN
	1    4100 2050
	1    0    0    -1  
$EndComp
Wire Wire Line
	4100 2050 4100 2150
Wire Wire Line
	4100 2150 4000 2150
$Comp
L Device:R_Small R9
U 1 1 61473525
P 4100 2500
F 0 "R9" H 4159 2546 50  0000 L CNN
F 1 "1K" H 4159 2455 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 4100 2500 50  0001 C CNN
F 3 "~" H 4100 2500 50  0001 C CNN
	1    4100 2500
	1    0    0    -1  
$EndComp
$Comp
L Device:R_Small R8
U 1 1 6147392E
P 3050 2500
F 0 "R8" H 3109 2546 50  0000 L CNN
F 1 "10K" H 3109 2455 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 3050 2500 50  0001 C CNN
F 3 "~" H 3050 2500 50  0001 C CNN
	1    3050 2500
	1    0    0    -1  
$EndComp
$Comp
L Device:R_Small R7
U 1 1 61473CD1
P 2750 2500
F 0 "R7" H 2809 2546 50  0000 L CNN
F 1 "20K" H 2809 2455 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 2750 2500 50  0001 C CNN
F 3 "~" H 2750 2500 50  0001 C CNN
	1    2750 2500
	1    0    0    -1  
$EndComp
Wire Wire Line
	3200 2350 3050 2350
Wire Wire Line
	2750 2350 2750 2400
Wire Wire Line
	3050 2400 3050 2350
Connection ~ 3050 2350
Wire Wire Line
	3050 2350 2750 2350
$Comp
L power:GND #PWR0120
U 1 1 614723F1
P 4100 1450
F 0 "#PWR0120" H 4100 1200 50  0001 C CNN
F 1 "GND" H 4105 1277 50  0000 C CNN
F 2 "" H 4100 1450 50  0001 C CNN
F 3 "" H 4100 1450 50  0001 C CNN
	1    4100 1450
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0121
U 1 1 614748FE
P 3600 2700
F 0 "#PWR0121" H 3600 2450 50  0001 C CNN
F 1 "GND" H 3605 2527 50  0000 C CNN
F 2 "" H 3600 2700 50  0001 C CNN
F 3 "" H 3600 2700 50  0001 C CNN
	1    3600 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	3600 2550 3600 2650
Wire Wire Line
	2750 2600 2750 2650
Wire Wire Line
	2750 2650 3050 2650
Connection ~ 3600 2650
Wire Wire Line
	3600 2650 3600 2700
Wire Wire Line
	3050 2600 3050 2650
Connection ~ 3050 2650
Wire Wire Line
	3050 2650 3600 2650
Wire Wire Line
	4000 2350 4100 2350
Wire Wire Line
	4100 2350 4100 2400
$Comp
L Device:LED D3
U 1 1 614762FA
P 4500 2350
F 0 "D3" H 4493 2567 50  0000 C CNN
F 1 "BLUE_LED" H 4493 2476 50  0000 C CNN
F 2 "LED_SMD:LED_0603_1608Metric_Pad1.05x0.95mm_HandSolder" H 4500 2350 50  0001 C CNN
F 3 "~" H 4500 2350 50  0001 C CNN
	1    4500 2350
	1    0    0    -1  
$EndComp
$Comp
L Device:LED D2
U 1 1 6147696E
P 4100 2800
F 0 "D2" V 4139 2682 50  0000 R CNN
F 1 "GREEN_LED" V 4048 2682 50  0000 R CNN
F 2 "LED_SMD:LED_0603_1608Metric_Pad1.05x0.95mm_HandSolder" H 4100 2800 50  0001 C CNN
F 3 "~" H 4100 2800 50  0001 C CNN
	1    4100 2800
	0    -1   -1   0   
$EndComp
Wire Wire Line
	4100 2600 4100 2650
Wire Wire Line
	4100 2950 4100 3000
Wire Wire Line
	4350 2350 4100 2350
Connection ~ 4100 2350
$Comp
L Device:R_Small R10
U 1 1 61478167
P 4800 2350
F 0 "R10" V 4604 2350 50  0000 C CNN
F 1 "1K" V 4695 2350 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 4800 2350 50  0001 C CNN
F 3 "~" H 4800 2350 50  0001 C CNN
	1    4800 2350
	0    1    1    0   
$EndComp
Wire Wire Line
	5000 2350 4900 2350
Wire Wire Line
	4700 2350 4650 2350
$Comp
L power:+BATT #PWR0124
U 1 1 61479D2E
P 9000 950
F 0 "#PWR0124" H 9000 800 50  0001 C CNN
F 1 "+BATT" H 9015 1123 50  0000 C CNN
F 2 "" H 9000 950 50  0001 C CNN
F 3 "" H 9000 950 50  0001 C CNN
	1    9000 950 
	1    0    0    -1  
$EndComp
$Comp
L Device:Battery_Cell BT1
U 1 1 6147A45D
P 9000 1600
F 0 "BT1" H 9118 1696 50  0000 L CNN
F 1 "3.7V" H 9118 1605 50  0000 L CNN
F 2 "battery:battery" V 9000 1660 50  0001 C CNN
F 3 "~" V 9000 1660 50  0001 C CNN
	1    9000 1600
	1    0    0    -1  
$EndComp
$Comp
L Device:CP1_Small C10
U 1 1 6147B039
P 9300 1050
F 0 "C10" V 9528 1050 50  0000 C CNN
F 1 "1.0 uF" V 9437 1050 50  0000 C CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 9300 1050 50  0001 C CNN
F 3 "~" H 9300 1050 50  0001 C CNN
	1    9300 1050
	0    -1   -1   0   
$EndComp
Wire Wire Line
	9000 950  9000 1050
Wire Wire Line
	9200 1050 9000 1050
Connection ~ 9000 1050
Wire Wire Line
	9000 1050 9000 1100
Wire Wire Line
	9000 1800 9000 1750
$Comp
L power:GND #PWR0126
U 1 1 6147D37F
P 9600 1100
F 0 "#PWR0126" H 9600 850 50  0001 C CNN
F 1 "GND" H 9605 927 50  0000 C CNN
F 2 "" H 9600 1100 50  0001 C CNN
F 3 "" H 9600 1100 50  0001 C CNN
	1    9600 1100
	1    0    0    -1  
$EndComp
Wire Wire Line
	9400 1050 9600 1050
Wire Wire Line
	9600 1050 9600 1100
Text Notes 5200 1950 2    39   ~ 0
Blue LED to indicate charging\n
Text Notes 5600 2750 2    39   ~ 0
Green LED to indicate charging complete\n\n
Text Notes 2550 2900 0    39   ~ 0
6.66 kOhm resistor for\ncharging current = 150 mA
Text Notes 4200 1300 0    39   ~ 0
TVS diode for input\nover-voltage protection
Text Notes 2000 1050 2    50   ~ 0
micro USB for charging
Text Notes 9400 750  2    50   ~ 0
3.7 V, 150 mAh battery\n
Text Notes 4250 750  2    50   ~ 0
Battery charger
$Comp
L LDO:ADP151 U7
U 1 1 614874E3
P 5450 4550
AR Path="/614874E3" Ref="U7"  Part="1" 
AR Path="/6146EBFE/614874E3" Ref="U7"  Part="1" 
F 0 "U7" H 5450 4965 50  0000 C CNN
F 1 "ADP151" H 5450 4874 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23-5_HandSoldering" H 5500 4850 50  0001 C CNN
F 3 "" H 5500 4850 50  0001 C CNN
	1    5450 4550
	1    0    0    -1  
$EndComp
Text HLabel 4850 4400 0    50   Input ~ 0
BATT_OUT
Wire Wire Line
	4850 4400 4950 4400
Wire Wire Line
	4950 4400 4950 4500
Wire Wire Line
	4950 4550 5050 4550
Connection ~ 4950 4400
Wire Wire Line
	4950 4400 5050 4400
$Comp
L power:GND #PWR0127
U 1 1 61488C00
P 4900 4800
F 0 "#PWR0127" H 4900 4550 50  0001 C CNN
F 1 "GND" H 4905 4627 50  0000 C CNN
F 2 "" H 4900 4800 50  0001 C CNN
F 3 "" H 4900 4800 50  0001 C CNN
	1    4900 4800
	1    0    0    -1  
$EndComp
Wire Wire Line
	4900 4800 4900 4750
Wire Wire Line
	4900 4700 5050 4700
$Comp
L Device:CP1_Small C8
U 1 1 61489AB7
P 4700 4650
F 0 "C8" H 4791 4696 50  0000 L CNN
F 1 "1 uF" H 4800 4600 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 4700 4650 50  0001 C CNN
F 3 "~" H 4700 4650 50  0001 C CNN
	1    4700 4650
	-1   0    0    -1  
$EndComp
Wire Wire Line
	4700 4550 4700 4500
Wire Wire Line
	4700 4500 4950 4500
Connection ~ 4950 4500
Wire Wire Line
	4950 4500 4950 4550
Wire Wire Line
	4700 4750 4900 4750
Connection ~ 4900 4750
Wire Wire Line
	4900 4750 4900 4700
$Comp
L power:+1V8 #PWR0128
U 1 1 6148D2E3
P 5950 4250
F 0 "#PWR0128" H 5950 4100 50  0001 C CNN
F 1 "+1V8" H 5965 4423 50  0000 C CNN
F 2 "" H 5950 4250 50  0001 C CNN
F 3 "" H 5950 4250 50  0001 C CNN
	1    5950 4250
	1    0    0    -1  
$EndComp
Wire Wire Line
	5950 4250 5950 4350
Wire Wire Line
	5950 4550 5850 4550
Text Notes 5300 3950 0    50   ~ 0
1.8V LDO\n
$Comp
L LDO:BU33SD5WG U8
U 1 1 6148E96D
P 8300 4500
F 0 "U8" H 8300 4925 50  0000 C CNN
F 1 "BU33SD5WG" H 8300 4834 50  0000 C CNN
F 2 "SSOP5:BU33SD5WG-TR" H 8300 4850 50  0001 C CNN
F 3 "" H 8300 4850 50  0001 C CNN
	1    8300 4500
	1    0    0    -1  
$EndComp
Text HLabel 7600 4300 0    50   Input ~ 0
BATT_OUT
Wire Wire Line
	7600 4300 7700 4300
Wire Wire Line
	7700 4300 7700 4450
Wire Wire Line
	7700 4500 7850 4500
Connection ~ 7700 4300
Wire Wire Line
	7700 4300 7850 4300
$Comp
L power:GND #PWR0129
U 1 1 61490710
P 7750 4800
F 0 "#PWR0129" H 7750 4550 50  0001 C CNN
F 1 "GND" H 7755 4627 50  0000 C CNN
F 2 "" H 7750 4800 50  0001 C CNN
F 3 "" H 7750 4800 50  0001 C CNN
	1    7750 4800
	1    0    0    -1  
$EndComp
Wire Wire Line
	7750 4800 7750 4750
Wire Wire Line
	7750 4700 7850 4700
$Comp
L Device:CP1_Small C11
U 1 1 61491CE7
P 7450 4600
F 0 "C11" H 7541 4646 50  0000 L CNN
F 1 "1 uF" H 7550 4550 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 7450 4600 50  0001 C CNN
F 3 "~" H 7450 4600 50  0001 C CNN
	1    7450 4600
	-1   0    0    -1  
$EndComp
Wire Wire Line
	7450 4500 7450 4450
Wire Wire Line
	7450 4450 7700 4450
Connection ~ 7700 4450
Wire Wire Line
	7700 4450 7700 4500
Wire Wire Line
	7450 4700 7450 4750
Wire Wire Line
	7450 4750 7750 4750
Connection ~ 7750 4750
Wire Wire Line
	7750 4750 7750 4700
$Comp
L power:+3.3V #PWR0130
U 1 1 6149486C
P 8850 4250
F 0 "#PWR0130" H 8850 4100 50  0001 C CNN
F 1 "+3.3V" H 8865 4423 50  0000 C CNN
F 2 "" H 8850 4250 50  0001 C CNN
F 3 "" H 8850 4250 50  0001 C CNN
	1    8850 4250
	1    0    0    -1  
$EndComp
Wire Wire Line
	8850 4250 8850 4300
Wire Wire Line
	8850 4500 8750 4500
$Comp
L Device:CP1_Small C12
U 1 1 61496051
P 9050 4650
F 0 "C12" H 9141 4696 50  0000 L CNN
F 1 "1 uF" H 9150 4600 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 9050 4650 50  0001 C CNN
F 3 "~" H 9050 4650 50  0001 C CNN
	1    9050 4650
	-1   0    0    -1  
$EndComp
Wire Wire Line
	9050 4550 9050 4450
Wire Wire Line
	9050 4450 8850 4450
Connection ~ 8850 4450
Wire Wire Line
	8850 4450 8850 4500
$Comp
L power:GND #PWR0131
U 1 1 61497A5F
P 9050 4800
F 0 "#PWR0131" H 9050 4550 50  0001 C CNN
F 1 "GND" H 9055 4627 50  0000 C CNN
F 2 "" H 9050 4800 50  0001 C CNN
F 3 "" H 9050 4800 50  0001 C CNN
	1    9050 4800
	1    0    0    -1  
$EndComp
Wire Wire Line
	9050 4800 9050 4750
$Comp
L Device:CP1_Small C9
U 1 1 6149911C
P 6100 4650
F 0 "C9" H 6191 4696 50  0000 L CNN
F 1 "1 uF" H 6200 4600 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 6100 4650 50  0001 C CNN
F 3 "~" H 6100 4650 50  0001 C CNN
	1    6100 4650
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0132
U 1 1 6149A98D
P 6100 4800
F 0 "#PWR0132" H 6100 4550 50  0001 C CNN
F 1 "GND" H 6105 4627 50  0000 C CNN
F 2 "" H 6100 4800 50  0001 C CNN
F 3 "" H 6100 4800 50  0001 C CNN
	1    6100 4800
	1    0    0    -1  
$EndComp
Wire Wire Line
	6100 4800 6100 4750
Wire Wire Line
	6100 4550 6100 4500
Wire Wire Line
	6100 4500 5950 4500
Connection ~ 5950 4500
Wire Wire Line
	5950 4500 5950 4550
Text Notes 8200 3850 0    50   ~ 0
3.3V LDO\n
$Comp
L voltage_sensor:S-1009N341-M5T1U U5
U 1 1 6149F33A
P 1850 4950
F 0 "U5" H 1875 5315 50  0000 C CNN
F 1 "S-1009N341-M5T1U" H 1875 5224 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23-5_HandSoldering" H 1900 6000 50  0001 C CNN
F 3 "" H 1900 6000 50  0001 C CNN
	1    1850 4950
	1    0    0    -1  
$EndComp
$Comp
L power:+BATT #PWR0133
U 1 1 6149F920
P 1350 4650
F 0 "#PWR0133" H 1350 4500 50  0001 C CNN
F 1 "+BATT" H 1365 4823 50  0000 C CNN
F 2 "" H 1350 4650 50  0001 C CNN
F 3 "" H 1350 4650 50  0001 C CNN
	1    1350 4650
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0134
U 1 1 6149FEEA
P 1350 5200
F 0 "#PWR0134" H 1350 4950 50  0001 C CNN
F 1 "GND" H 1355 5027 50  0000 C CNN
F 2 "" H 1350 5200 50  0001 C CNN
F 3 "" H 1350 5200 50  0001 C CNN
	1    1350 5200
	1    0    0    -1  
$EndComp
Wire Wire Line
	1350 4650 1350 4800
Wire Wire Line
	1350 4850 1500 4850
Wire Wire Line
	1350 5200 1350 5150
Wire Wire Line
	1350 5100 1500 5100
$Comp
L Device:R_Small R5
U 1 1 614A4BB7
P 1100 4950
F 0 "R5" H 1159 4996 50  0000 L CNN
F 1 "100k" H 1159 4905 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 1100 4950 50  0001 C CNN
F 3 "~" H 1100 4950 50  0001 C CNN
	1    1100 4950
	1    0    0    -1  
$EndComp
Wire Wire Line
	1100 4850 1100 4800
Wire Wire Line
	1100 4800 1350 4800
Connection ~ 1350 4800
Wire Wire Line
	1350 4800 1350 4850
Wire Wire Line
	1100 5050 1100 5150
Wire Wire Line
	1100 5150 1350 5150
Connection ~ 1350 5150
Wire Wire Line
	1350 5150 1350 5100
$Comp
L Device:C_Small C7
U 1 1 614A8A0E
P 2350 5250
F 0 "C7" H 2258 5204 50  0000 R CNN
F 1 "1.8 nF" H 2258 5295 50  0000 R CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 2350 5250 50  0001 C CNN
F 3 "~" H 2350 5250 50  0001 C CNN
	1    2350 5250
	-1   0    0    1   
$EndComp
Wire Wire Line
	2250 5100 2350 5100
Wire Wire Line
	2350 5100 2350 5150
$Comp
L power:GND #PWR0135
U 1 1 614AAAC4
P 2350 5350
F 0 "#PWR0135" H 2350 5100 50  0001 C CNN
F 1 "GND" H 2355 5177 50  0000 C CNN
F 2 "" H 2350 5350 50  0001 C CNN
F 3 "" H 2350 5350 50  0001 C CNN
	1    2350 5350
	1    0    0    -1  
$EndComp
Wire Wire Line
	2650 4850 2250 4850
$Comp
L power:GND #PWR0136
U 1 1 614B170A
P 2950 5100
F 0 "#PWR0136" H 2950 4850 50  0001 C CNN
F 1 "GND" H 2955 4927 50  0000 C CNN
F 2 "" H 2950 5100 50  0001 C CNN
F 3 "" H 2950 5100 50  0001 C CNN
	1    2950 5100
	1    0    0    -1  
$EndComp
Wire Wire Line
	2950 5050 2950 5100
Wire Wire Line
	2950 4450 2950 4550
$Comp
L power:+BATT #PWR0137
U 1 1 614B6C3C
P 2500 4000
F 0 "#PWR0137" H 2500 3850 50  0001 C CNN
F 1 "+BATT" H 2515 4173 50  0000 C CNN
F 2 "" H 2500 4000 50  0001 C CNN
F 3 "" H 2500 4000 50  0001 C CNN
	1    2500 4000
	1    0    0    -1  
$EndComp
Wire Wire Line
	2500 4000 2500 4150
Wire Wire Line
	2500 4150 2750 4150
$Comp
L Device:R_Small R6
U 1 1 614B9265
P 2500 4350
F 0 "R6" H 2559 4396 50  0000 L CNN
F 1 "100K" H 2559 4305 50  0000 L CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 2500 4350 50  0001 C CNN
F 3 "~" H 2500 4350 50  0001 C CNN
	1    2500 4350
	1    0    0    -1  
$EndComp
Wire Wire Line
	2500 4150 2500 4250
Connection ~ 2500 4150
Wire Wire Line
	2500 4450 2500 4550
Wire Wire Line
	2500 4550 2950 4550
Connection ~ 2950 4550
Wire Wire Line
	2950 4550 2950 4650
Text HLabel 3650 4150 2    50   Output ~ 0
BATT_OUT
Wire Wire Line
	3150 4150 3300 4150
Text Notes 1700 3700 0    50   ~ 0
Low voltage battery shutdown
$Comp
L power:PWR_FLAG #FLG0101
U 1 1 61503639
P 3450 4050
F 0 "#FLG0101" H 3450 4125 50  0001 C CNN
F 1 "PWR_FLAG" H 3450 4223 50  0000 C CNN
F 2 "" H 3450 4050 50  0001 C CNN
F 3 "~" H 3450 4050 50  0001 C CNN
	1    3450 4050
	1    0    0    -1  
$EndComp
Wire Wire Line
	3450 4050 3450 4150
Connection ~ 3450 4150
Wire Wire Line
	3450 4150 3650 4150
Wire Wire Line
	1850 1850 1800 1850
Text HLabel 3650 1050 2    50   Input ~ 0
VBUS
Wire Wire Line
	3600 1050 3650 1050
Wire Wire Line
	3600 1050 3600 1250
Text HLabel 5000 2350 2    50   Input ~ 0
VBUS
$Comp
L DW01_battery_protection:DW01 U4
U 1 1 61EFC707
P 7050 1600
F 0 "U4" H 7075 2015 50  0000 C CNN
F 1 "DW01" H 7075 1924 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23-6_Handsoldering" H 6700 1900 50  0001 C CNN
F 3 "" H 6700 1900 50  0001 C CNN
	1    7050 1600
	1    0    0    -1  
$EndComp
$Comp
L DMG9926USD_NMOS:DMG9926USD U3
U 1 1 61F056CC
P 5900 1500
F 0 "U3" H 5925 1965 50  0000 C CNN
F 1 "DMG9926USD" H 5925 1874 50  0000 C CNN
F 2 "Package_SO:SOP-8_3.9x4.9mm_P1.27mm" H 5850 2100 50  0001 C CNN
F 3 "" H 5850 2100 50  0001 C CNN
	1    5900 1500
	-1   0    0    -1  
$EndComp
$Comp
L Device:R_Small R19
U 1 1 61F06616
P 7800 1450
F 0 "R19" V 7604 1450 50  0000 C CNN
F 1 "100" V 7695 1450 50  0000 C CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 7800 1450 50  0001 C CNN
F 3 "~" H 7800 1450 50  0001 C CNN
	1    7800 1450
	0    1    1    0   
$EndComp
NoConn ~ 7450 1600
$Comp
L Device:C_Small C6
U 1 1 61F0A09B
P 7600 1600
F 0 "C6" H 7692 1646 50  0000 L CNN
F 1 "0.1u" H 7692 1555 50  0000 L CNN
F 2 "Capacitor_SMD:C_0603_1608Metric_Pad1.08x0.95mm_HandSolder" H 7600 1600 50  0001 C CNN
F 3 "~" H 7600 1600 50  0001 C CNN
	1    7600 1600
	1    0    0    -1  
$EndComp
Wire Wire Line
	7450 1450 7500 1450
Wire Wire Line
	7600 1500 7600 1450
Connection ~ 7600 1450
Wire Wire Line
	7600 1450 7700 1450
Wire Wire Line
	7600 1700 7600 1750
Wire Wire Line
	7600 1750 7450 1750
$Comp
L Device:R_Small R18
U 1 1 61F23105
P 6600 2100
F 0 "R18" H 6541 2054 50  0000 R CNN
F 1 "1k" H 6541 2145 50  0000 R CNN
F 2 "Resistor_SMD:R_0603_1608Metric_Pad0.98x0.95mm_HandSolder" H 6600 2100 50  0001 C CNN
F 3 "~" H 6600 2100 50  0001 C CNN
	1    6600 2100
	-1   0    0    1   
$EndComp
Wire Wire Line
	6700 1750 6600 1750
$Comp
L power:GND #PWR0122
U 1 1 614775C5
P 4100 3000
F 0 "#PWR0122" H 4100 2750 50  0001 C CNN
F 1 "GND" H 4105 2827 50  0000 C CNN
F 2 "" H 4100 3000 50  0001 C CNN
F 3 "" H 4100 3000 50  0001 C CNN
	1    4100 3000
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR0118
U 1 1 61F2A521
P 6600 2300
F 0 "#PWR0118" H 6600 2050 50  0001 C CNN
F 1 "GND" H 6605 2127 50  0000 C CNN
F 2 "" H 6600 2300 50  0001 C CNN
F 3 "" H 6600 2300 50  0001 C CNN
	1    6600 2300
	1    0    0    -1  
$EndComp
Wire Wire Line
	6600 2300 6600 2200
Wire Wire Line
	5550 1750 5500 1750
Wire Wire Line
	5500 1750 5500 1600
Wire Wire Line
	5500 1600 5550 1600
Wire Wire Line
	5550 1450 5500 1450
Wire Wire Line
	5500 1450 5500 1600
Connection ~ 5500 1600
Wire Wire Line
	5550 1300 5500 1300
Wire Wire Line
	5500 1300 5500 1450
Connection ~ 5500 1450
$Comp
L power:-BATT #PWR0123
U 1 1 61F54865
P 6400 1200
F 0 "#PWR0123" H 6400 1050 50  0001 C CNN
F 1 "-BATT" H 6415 1373 50  0000 C CNN
F 2 "" H 6400 1200 50  0001 C CNN
F 3 "" H 6400 1200 50  0001 C CNN
	1    6400 1200
	-1   0    0    1   
$EndComp
Wire Wire Line
	6200 1300 6250 1300
Wire Wire Line
	6250 1300 6250 1100
Wire Wire Line
	6250 1100 6400 1100
Wire Wire Line
	6400 1100 6400 1200
$Comp
L power:-BATT #PWR0125
U 1 1 61F5BFED
P 9000 1800
F 0 "#PWR0125" H 9000 1650 50  0001 C CNN
F 1 "-BATT" H 9015 1973 50  0000 C CNN
F 2 "" H 9000 1800 50  0001 C CNN
F 3 "" H 9000 1800 50  0001 C CNN
	1    9000 1800
	-1   0    0    1   
$EndComp
$Comp
L power:+BATT #PWR0145
U 1 1 61F5CD61
P 8050 1400
F 0 "#PWR0145" H 8050 1250 50  0001 C CNN
F 1 "+BATT" H 8065 1573 50  0000 C CNN
F 2 "" H 8050 1400 50  0001 C CNN
F 3 "" H 8050 1400 50  0001 C CNN
	1    8050 1400
	1    0    0    -1  
$EndComp
Wire Wire Line
	8050 1400 8050 1450
Wire Wire Line
	8050 1450 7900 1450
Wire Wire Line
	6600 1750 6600 2000
Wire Wire Line
	6200 1450 6700 1450
Wire Wire Line
	6200 1600 6700 1600
$Comp
L power:GND #PWR0146
U 1 1 61F741A9
P 6250 1900
F 0 "#PWR0146" H 6250 1650 50  0001 C CNN
F 1 "GND" H 6255 1727 50  0000 C CNN
F 2 "" H 6250 1900 50  0001 C CNN
F 3 "" H 6250 1900 50  0001 C CNN
	1    6250 1900
	1    0    0    -1  
$EndComp
Wire Wire Line
	6200 1750 6250 1750
Wire Wire Line
	6250 1750 6250 1900
Text Notes 5250 900  0    39   ~ 0
NMOS transistors for over current and over discharge protection.\nUndernormal operation, transistors are shorted together and BATT- = GND
$Comp
L power:GND #PWR0117
U 1 1 6146FFA2
P 1850 2250
F 0 "#PWR0117" H 1850 2000 50  0001 C CNN
F 1 "GND" H 1855 2077 50  0000 C CNN
F 2 "" H 1850 2250 50  0001 C CNN
F 3 "" H 1850 2250 50  0001 C CNN
	1    1850 2250
	1    0    0    -1  
$EndComp
Wire Wire Line
	1850 1850 1850 1950
$Comp
L power:PWR_FLAG #FLG0102
U 1 1 61FB428D
P 3500 1250
F 0 "#FLG0102" H 3500 1325 50  0001 C CNN
F 1 "PWR_FLAG" V 3500 1377 50  0000 L CNN
F 2 "" H 3500 1250 50  0001 C CNN
F 3 "~" H 3500 1250 50  0001 C CNN
	1    3500 1250
	0    -1   -1   0   
$EndComp
Wire Wire Line
	3500 1250 3600 1250
Connection ~ 3600 1250
Wire Wire Line
	3600 1250 3600 1400
$Comp
L power:PWR_FLAG #FLG0103
U 1 1 61FB97F6
P 1950 2050
F 0 "#FLG0103" H 1950 2125 50  0001 C CNN
F 1 "PWR_FLAG" V 1950 2178 50  0000 L CNN
F 2 "" H 1950 2050 50  0001 C CNN
F 3 "~" H 1950 2050 50  0001 C CNN
	1    1950 2050
	0    1    1    0   
$EndComp
Wire Wire Line
	1950 2050 1850 2050
Connection ~ 1850 2050
Wire Wire Line
	1850 2050 1850 2200
$Comp
L power:-BATT #PWR0147
U 1 1 61FC947D
P 7600 1900
F 0 "#PWR0147" H 7600 1750 50  0001 C CNN
F 1 "-BATT" H 7615 2073 50  0000 C CNN
F 2 "" H 7600 1900 50  0001 C CNN
F 3 "" H 7600 1900 50  0001 C CNN
	1    7600 1900
	-1   0    0    1   
$EndComp
Wire Wire Line
	7600 1900 7600 1850
Connection ~ 7600 1750
$Comp
L power:PWR_FLAG #FLG0104
U 1 1 61FCEBD9
P 7700 1850
F 0 "#FLG0104" H 7700 1925 50  0001 C CNN
F 1 "PWR_FLAG" V 7700 1978 50  0000 L CNN
F 2 "" H 7700 1850 50  0001 C CNN
F 3 "~" H 7700 1850 50  0001 C CNN
	1    7700 1850
	0    1    1    0   
$EndComp
Wire Wire Line
	7700 1850 7600 1850
Connection ~ 7600 1850
Wire Wire Line
	7600 1850 7600 1750
$Comp
L power:PWR_FLAG #FLG0105
U 1 1 61FD9A04
P 7500 1300
F 0 "#FLG0105" H 7500 1375 50  0001 C CNN
F 1 "PWR_FLAG" H 7500 1473 50  0000 C CNN
F 2 "" H 7500 1300 50  0001 C CNN
F 3 "~" H 7500 1300 50  0001 C CNN
	1    7500 1300
	1    0    0    -1  
$EndComp
Wire Wire Line
	7500 1300 7500 1450
Connection ~ 7500 1450
Wire Wire Line
	7500 1450 7600 1450
$Comp
L Device:Fuse_Small F1
U 1 1 620340E9
P 9000 1250
F 0 "F1" V 8954 1298 50  0000 L CNN
F 1 "300 mA" V 9045 1298 50  0000 L CNN
F 2 "Fuse:Fuse_0603_1608Metric_Pad1.05x0.95mm_HandSolder" H 9000 1250 50  0001 C CNN
F 3 "~" H 9000 1250 50  0001 C CNN
	1    9000 1250
	0    1    1    0   
$EndComp
Wire Wire Line
	9000 1350 9000 1400
$Comp
L Connector:TestPoint TP4
U 1 1 620409C5
P 8800 1100
F 0 "TP4" V 8995 1172 50  0000 C CNN
F 1 "TestPoint" V 8904 1172 50  0000 C CNN
F 2 "TestPoint:TestPoint_Loop_D2.50mm_Drill1.0mm_LowProfile" H 9000 1100 50  0001 C CNN
F 3 "~" H 9000 1100 50  0001 C CNN
	1    8800 1100
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8800 1100 9000 1100
Connection ~ 9000 1100
Wire Wire Line
	9000 1100 9000 1150
$Comp
L Connector:TestPoint TP5
U 1 1 62046C2E
P 8800 1750
F 0 "TP5" V 8995 1822 50  0000 C CNN
F 1 "TestPoint" V 8904 1822 50  0000 C CNN
F 2 "TestPoint:TestPoint_Loop_D2.50mm_Drill1.0mm_LowProfile" H 9000 1750 50  0001 C CNN
F 3 "~" H 9000 1750 50  0001 C CNN
	1    8800 1750
	0    -1   -1   0   
$EndComp
Wire Wire Line
	8800 1750 9000 1750
Connection ~ 9000 1750
Wire Wire Line
	9000 1750 9000 1700
$Comp
L Connector:TestPoint TP1
U 1 1 6204CE6B
P 2000 2200
F 0 "TP1" V 1954 2388 50  0000 L CNN
F 1 "TestPoint" V 2045 2388 50  0000 L CNN
F 2 "TestPoint:TestPoint_Loop_D2.50mm_Drill1.0mm_LowProfile" H 2200 2200 50  0001 C CNN
F 3 "~" H 2200 2200 50  0001 C CNN
	1    2000 2200
	0    1    1    0   
$EndComp
$Comp
L Connector:TestPoint TP2
U 1 1 620547A7
P 3300 4250
F 0 "TP2" H 3242 4276 50  0000 R CNN
F 1 "TestPoint" H 3242 4367 50  0000 R CNN
F 2 "TestPoint:TestPoint_Loop_D2.50mm_Drill1.0mm_LowProfile" H 3500 4250 50  0001 C CNN
F 3 "~" H 3500 4250 50  0001 C CNN
	1    3300 4250
	-1   0    0    1   
$EndComp
Wire Wire Line
	3300 4250 3300 4150
Connection ~ 3300 4150
Wire Wire Line
	3300 4150 3450 4150
$Comp
L Connector:TestPoint TP3
U 1 1 6205AD47
P 6050 4350
F 0 "TP3" V 6004 4538 50  0000 L CNN
F 1 "TestPoint" V 6095 4538 50  0000 L CNN
F 2 "TestPoint:TestPoint_Loop_D2.50mm_Drill1.0mm_LowProfile" H 6250 4350 50  0001 C CNN
F 3 "~" H 6250 4350 50  0001 C CNN
	1    6050 4350
	0    1    1    0   
$EndComp
Wire Wire Line
	6050 4350 5950 4350
Connection ~ 5950 4350
Wire Wire Line
	5950 4350 5950 4500
$Comp
L Connector:TestPoint TP6
U 1 1 620678C6
P 8950 4300
F 0 "TP6" V 8904 4488 50  0000 L CNN
F 1 "TestPoint" V 8995 4488 50  0000 L CNN
F 2 "TestPoint:TestPoint_Loop_D2.50mm_Drill1.0mm_LowProfile" H 9150 4300 50  0001 C CNN
F 3 "~" H 9150 4300 50  0001 C CNN
	1    8950 4300
	0    1    1    0   
$EndComp
Wire Wire Line
	8950 4300 8850 4300
Connection ~ 8850 4300
Wire Wire Line
	8850 4300 8850 4450
Wire Wire Line
	1800 1550 1900 1550
Text HLabel 1900 1550 2    50   Output ~ 0
VBUS
$Comp
L KMMVX-SMT-BS-BTR:KMMVX-SMT-BS-BTR P1
U 1 1 620CDE6B
P 1600 1750
F 0 "P1" H 1643 2217 50  0000 C CNN
F 1 "KMMVX-SMT-BS-BTR" H 1643 2126 50  0000 C CNN
F 2 "micro_usb:KYCON_KMMVX-SMT-BS-BTR" H 1600 1750 50  0001 L BNN
F 3 "" H 1600 1750 50  0001 L BNN
F 4 "Kycon" H 1600 1750 50  0001 L BNN "MANUFACTURER"
	1    1600 1750
	-1   0    0    -1  
$EndComp
Text HLabel 1900 1650 2    50   Output ~ 0
D+
Text HLabel 1900 1750 2    50   Output ~ 0
D-
Wire Wire Line
	1900 1750 1800 1750
Wire Wire Line
	1900 1650 1800 1650
Wire Wire Line
	1800 1950 1850 1950
Connection ~ 1850 1950
Wire Wire Line
	1850 1950 1850 2050
Wire Wire Line
	2000 2200 1850 2200
Connection ~ 1850 2200
Wire Wire Line
	1850 2200 1850 2250
$Comp
L Device:Q_PMOS_DGS Q2
U 1 1 614B3DBE
P 2950 4250
F 0 "Q2" V 3292 4250 50  0000 C CNN
F 1 "Q_PMOS_DGS" V 3201 4250 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:SOT-23" H 3150 4350 50  0001 C CNN
F 3 "~" H 2950 4250 50  0001 C CNN
	1    2950 4250
	0    1    -1   0   
$EndComp
$Comp
L Device:Q_NMOS_DGS Q1
U 1 1 614AE6C2
P 2850 4850
F 0 "Q1" H 3054 4896 50  0000 L CNN
F 1 "Q_NMOS_DGS" H 3054 4805 50  0000 L CNN
F 2 "Package_TO_SOT_SMD:SOT-23" H 3050 4950 50  0001 C CNN
F 3 "~" H 2850 4850 50  0001 C CNN
	1    2850 4850
	1    0    0    -1  
$EndComp
$EndSCHEMATC