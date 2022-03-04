EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 3 3
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
Text Notes 3900 1950 2    50   ~ 0
Connections to motors\n\n
$Comp
L Connector:Conn_01x04_Male J?
U 1 1 622E5B31
P 2750 2250
F 0 "J?" H 2858 2531 50  0000 C CNN
F 1 "STEPPER_1" H 2858 2440 50  0000 C CNN
F 2 "" H 2750 2250 50  0001 C CNN
F 3 "~" H 2750 2250 50  0001 C CNN
	1    2750 2250
	1    0    0    -1  
$EndComp
Wire Wire Line
	3050 2150 2950 2150
Wire Wire Line
	3050 2250 2950 2250
Wire Wire Line
	3050 2350 2950 2350
Wire Wire Line
	3050 2450 2950 2450
$Comp
L Connector:Conn_01x04_Male J?
U 1 1 622E5B3B
P 2750 2900
F 0 "J?" H 2858 3181 50  0000 C CNN
F 1 "STEPPER_2" H 2858 3090 50  0000 C CNN
F 2 "" H 2750 2900 50  0001 C CNN
F 3 "~" H 2750 2900 50  0001 C CNN
	1    2750 2900
	1    0    0    -1  
$EndComp
Text GLabel 3050 2800 2    50   Input ~ 0
STEPPER_2_1
Wire Wire Line
	3050 2800 2950 2800
Text GLabel 3050 2900 2    50   Input ~ 0
STEPPER_2_2
Text GLabel 3050 3000 2    50   Input ~ 0
STEPPER_2_3
Text GLabel 3050 3100 2    50   Input ~ 0
STEPPER_2_4
Wire Wire Line
	3050 2900 2950 2900
Wire Wire Line
	3050 3000 2950 3000
Wire Wire Line
	3050 3100 2950 3100
$Comp
L Connector:Conn_01x04_Male J?
U 1 1 622E5B49
P 2750 3600
F 0 "J?" H 2858 3881 50  0000 C CNN
F 1 "STEPPER_3" H 2858 3790 50  0000 C CNN
F 2 "" H 2750 3600 50  0001 C CNN
F 3 "~" H 2750 3600 50  0001 C CNN
	1    2750 3600
	1    0    0    -1  
$EndComp
Text GLabel 3050 3500 2    50   Input ~ 0
STEPPER_3_1
Wire Wire Line
	3050 3500 2950 3500
Text GLabel 3050 3600 2    50   Input ~ 0
STEPPER_3_2
Text GLabel 3050 3700 2    50   Input ~ 0
STEPPER_3_3
Text GLabel 3050 3800 2    50   Input ~ 0
STEPPER_3_4
Wire Wire Line
	3050 3600 2950 3600
Wire Wire Line
	3050 3700 2950 3700
Wire Wire Line
	3050 3800 2950 3800
$Comp
L Connector:Conn_01x04_Male J?
U 1 1 622E5B57
P 2750 4450
F 0 "J?" H 2858 4731 50  0000 C CNN
F 1 "STEPPER_4" H 2858 4640 50  0000 C CNN
F 2 "" H 2750 4450 50  0001 C CNN
F 3 "~" H 2750 4450 50  0001 C CNN
	1    2750 4450
	1    0    0    -1  
$EndComp
Text GLabel 3050 4350 2    50   Input ~ 0
STEPPER_4_1
Wire Wire Line
	3050 4350 2950 4350
Text GLabel 3050 4450 2    50   Input ~ 0
STEPPER_4_2
Text GLabel 3050 4550 2    50   Input ~ 0
STEPPER_4_3
Text GLabel 3050 4650 2    50   Input ~ 0
STEPPER_4_4
Wire Wire Line
	3050 4450 2950 4450
Wire Wire Line
	3050 4550 2950 4550
Wire Wire Line
	3050 4650 2950 4650
$Comp
L Connector:Conn_01x04_Male J?
U 1 1 622E5B65
P 2700 5250
F 0 "J?" H 2808 5531 50  0000 C CNN
F 1 "STEPPER_5" H 2808 5440 50  0000 C CNN
F 2 "" H 2700 5250 50  0001 C CNN
F 3 "~" H 2700 5250 50  0001 C CNN
	1    2700 5250
	1    0    0    -1  
$EndComp
Text GLabel 3000 5150 2    50   Input ~ 0
STEPPER_5_1
Wire Wire Line
	3000 5150 2900 5150
Text GLabel 3000 5250 2    50   Input ~ 0
STEPPER_5_2
Text GLabel 3000 5350 2    50   Input ~ 0
STEPPER_5_3
Text GLabel 3000 5450 2    50   Input ~ 0
STEPPER_5_4
Wire Wire Line
	3000 5250 2900 5250
Wire Wire Line
	3000 5350 2900 5350
Wire Wire Line
	3000 5450 2900 5450
Text GLabel 3050 2150 2    50   Input ~ 0
STEPPER_1_1
Text GLabel 3050 2250 2    50   Input ~ 0
STEPPER_1_2
Text GLabel 3050 2350 2    50   Input ~ 0
STEPPER_1_3
Text GLabel 3050 2450 2    50   Input ~ 0
STEPPER_1_4
$Comp
L Connector:Conn_01x02_Male J?
U 1 1 622E5B77
P 3950 2200
F 0 "J?" H 4058 2381 50  0000 C CNN
F 1 "STEPPER_1_PWR" H 4058 2290 50  0000 C CNN
F 2 "" H 3950 2200 50  0001 C CNN
F 3 "~" H 3950 2200 50  0001 C CNN
	1    3950 2200
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR?
U 1 1 622E5B7D
P 4500 2100
F 0 "#PWR?" H 4500 1950 50  0001 C CNN
F 1 "+5V" H 4515 2273 50  0000 C CNN
F 2 "" H 4500 2100 50  0001 C CNN
F 3 "" H 4500 2100 50  0001 C CNN
	1    4500 2100
	1    0    0    -1  
$EndComp
Wire Wire Line
	4500 2100 4500 2150
Wire Wire Line
	4500 2200 4150 2200
$Comp
L power:GND #PWR?
U 1 1 622E5B85
P 4500 2400
F 0 "#PWR?" H 4500 2150 50  0001 C CNN
F 1 "GND" H 4505 2227 50  0000 C CNN
F 2 "" H 4500 2400 50  0001 C CNN
F 3 "" H 4500 2400 50  0001 C CNN
	1    4500 2400
	1    0    0    -1  
$EndComp
Wire Wire Line
	4500 2400 4500 2350
Wire Wire Line
	4500 2300 4150 2300
$Comp
L Device:CP1_Small C?
U 1 1 622E5B8D
P 4650 2250
F 0 "C?" H 4741 2296 50  0000 L CNN
F 1 "1 uF" H 4741 2205 50  0000 L CNN
F 2 "" H 4650 2250 50  0001 C CNN
F 3 "~" H 4650 2250 50  0001 C CNN
	1    4650 2250
	1    0    0    -1  
$EndComp
Wire Wire Line
	4650 2150 4500 2150
Connection ~ 4500 2150
Wire Wire Line
	4500 2150 4500 2200
Wire Wire Line
	4650 2350 4500 2350
Connection ~ 4500 2350
Wire Wire Line
	4500 2350 4500 2300
$Comp
L Connector:Conn_01x02_Male J?
U 1 1 622E5B99
P 3950 2950
F 0 "J?" H 4058 3131 50  0000 C CNN
F 1 "STEPPER_2_PWR" H 4058 3040 50  0000 C CNN
F 2 "" H 3950 2950 50  0001 C CNN
F 3 "~" H 3950 2950 50  0001 C CNN
	1    3950 2950
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR?
U 1 1 622E5B9F
P 4500 2850
F 0 "#PWR?" H 4500 2700 50  0001 C CNN
F 1 "+5V" H 4515 3023 50  0000 C CNN
F 2 "" H 4500 2850 50  0001 C CNN
F 3 "" H 4500 2850 50  0001 C CNN
	1    4500 2850
	1    0    0    -1  
$EndComp
Wire Wire Line
	4500 2850 4500 2900
Wire Wire Line
	4500 2950 4150 2950
$Comp
L power:GND #PWR?
U 1 1 622E5BA7
P 4500 3150
F 0 "#PWR?" H 4500 2900 50  0001 C CNN
F 1 "GND" H 4505 2977 50  0000 C CNN
F 2 "" H 4500 3150 50  0001 C CNN
F 3 "" H 4500 3150 50  0001 C CNN
	1    4500 3150
	1    0    0    -1  
$EndComp
Wire Wire Line
	4500 3150 4500 3100
Wire Wire Line
	4500 3050 4150 3050
$Comp
L Device:CP1_Small C?
U 1 1 622E5BAF
P 4650 3000
F 0 "C?" H 4741 3046 50  0000 L CNN
F 1 "1 uF" H 4741 2955 50  0000 L CNN
F 2 "" H 4650 3000 50  0001 C CNN
F 3 "~" H 4650 3000 50  0001 C CNN
	1    4650 3000
	1    0    0    -1  
$EndComp
Wire Wire Line
	4650 2900 4500 2900
Connection ~ 4500 2900
Wire Wire Line
	4500 2900 4500 2950
Wire Wire Line
	4650 3100 4500 3100
Connection ~ 4500 3100
Wire Wire Line
	4500 3100 4500 3050
$Comp
L power:+5V #PWR?
U 1 1 622E5BBB
P 4400 3600
F 0 "#PWR?" H 4400 3450 50  0001 C CNN
F 1 "+5V" H 4415 3773 50  0000 C CNN
F 2 "" H 4400 3600 50  0001 C CNN
F 3 "" H 4400 3600 50  0001 C CNN
	1    4400 3600
	1    0    0    -1  
$EndComp
Wire Wire Line
	4400 3600 4400 3650
Wire Wire Line
	4400 3700 4050 3700
$Comp
L power:GND #PWR?
U 1 1 622E5BC3
P 4400 3900
F 0 "#PWR?" H 4400 3650 50  0001 C CNN
F 1 "GND" H 4405 3727 50  0000 C CNN
F 2 "" H 4400 3900 50  0001 C CNN
F 3 "" H 4400 3900 50  0001 C CNN
	1    4400 3900
	1    0    0    -1  
$EndComp
Wire Wire Line
	4400 3900 4400 3850
Wire Wire Line
	4400 3800 4050 3800
$Comp
L Device:CP1_Small C?
U 1 1 622E5BCB
P 4550 3750
F 0 "C?" H 4641 3796 50  0000 L CNN
F 1 "1 uF" H 4641 3705 50  0000 L CNN
F 2 "" H 4550 3750 50  0001 C CNN
F 3 "~" H 4550 3750 50  0001 C CNN
	1    4550 3750
	1    0    0    -1  
$EndComp
Wire Wire Line
	4550 3650 4400 3650
Connection ~ 4400 3650
Wire Wire Line
	4400 3650 4400 3700
Wire Wire Line
	4550 3850 4400 3850
Connection ~ 4400 3850
Wire Wire Line
	4400 3850 4400 3800
$Comp
L Connector:Conn_01x02_Male J?
U 1 1 622E5BD7
P 3850 4500
F 0 "J?" H 3958 4681 50  0000 C CNN
F 1 "STEPPER_4_PWR" H 3958 4590 50  0000 C CNN
F 2 "" H 3850 4500 50  0001 C CNN
F 3 "~" H 3850 4500 50  0001 C CNN
	1    3850 4500
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR?
U 1 1 622E5BDD
P 4400 4400
F 0 "#PWR?" H 4400 4250 50  0001 C CNN
F 1 "+5V" H 4415 4573 50  0000 C CNN
F 2 "" H 4400 4400 50  0001 C CNN
F 3 "" H 4400 4400 50  0001 C CNN
	1    4400 4400
	1    0    0    -1  
$EndComp
Wire Wire Line
	4400 4400 4400 4450
Wire Wire Line
	4400 4500 4050 4500
$Comp
L power:GND #PWR?
U 1 1 622E5BE5
P 4400 4700
F 0 "#PWR?" H 4400 4450 50  0001 C CNN
F 1 "GND" H 4405 4527 50  0000 C CNN
F 2 "" H 4400 4700 50  0001 C CNN
F 3 "" H 4400 4700 50  0001 C CNN
	1    4400 4700
	1    0    0    -1  
$EndComp
Wire Wire Line
	4400 4700 4400 4650
Wire Wire Line
	4400 4600 4050 4600
$Comp
L Device:CP1_Small C?
U 1 1 622E5BED
P 4550 4550
F 0 "C?" H 4641 4596 50  0000 L CNN
F 1 "1 uF" H 4641 4505 50  0000 L CNN
F 2 "" H 4550 4550 50  0001 C CNN
F 3 "~" H 4550 4550 50  0001 C CNN
	1    4550 4550
	1    0    0    -1  
$EndComp
Wire Wire Line
	4550 4450 4400 4450
Connection ~ 4400 4450
Wire Wire Line
	4400 4450 4400 4500
Wire Wire Line
	4550 4650 4400 4650
Connection ~ 4400 4650
Wire Wire Line
	4400 4650 4400 4600
$Comp
L Connector:Conn_01x02_Male J?
U 1 1 622E5BF9
P 3900 5350
F 0 "J?" H 4008 5531 50  0000 C CNN
F 1 "STEPPER_5_PWR" H 4008 5440 50  0000 C CNN
F 2 "" H 3900 5350 50  0001 C CNN
F 3 "~" H 3900 5350 50  0001 C CNN
	1    3900 5350
	1    0    0    -1  
$EndComp
$Comp
L power:+5V #PWR?
U 1 1 622E5BFF
P 4450 5250
F 0 "#PWR?" H 4450 5100 50  0001 C CNN
F 1 "+5V" H 4465 5423 50  0000 C CNN
F 2 "" H 4450 5250 50  0001 C CNN
F 3 "" H 4450 5250 50  0001 C CNN
	1    4450 5250
	1    0    0    -1  
$EndComp
Wire Wire Line
	4450 5250 4450 5300
Wire Wire Line
	4450 5350 4100 5350
$Comp
L power:GND #PWR?
U 1 1 622E5C07
P 4450 5550
F 0 "#PWR?" H 4450 5300 50  0001 C CNN
F 1 "GND" H 4455 5377 50  0000 C CNN
F 2 "" H 4450 5550 50  0001 C CNN
F 3 "" H 4450 5550 50  0001 C CNN
	1    4450 5550
	1    0    0    -1  
$EndComp
Wire Wire Line
	4450 5550 4450 5500
Wire Wire Line
	4450 5450 4100 5450
$Comp
L Device:CP1_Small C?
U 1 1 622E5C0F
P 4600 5400
F 0 "C?" H 4691 5446 50  0000 L CNN
F 1 "1 uF" H 4691 5355 50  0000 L CNN
F 2 "" H 4600 5400 50  0001 C CNN
F 3 "~" H 4600 5400 50  0001 C CNN
	1    4600 5400
	1    0    0    -1  
$EndComp
Wire Wire Line
	4600 5300 4450 5300
Connection ~ 4450 5300
Wire Wire Line
	4450 5300 4450 5350
Wire Wire Line
	4600 5500 4450 5500
Connection ~ 4450 5500
Wire Wire Line
	4450 5500 4450 5450
$Comp
L Connector:Conn_01x02_Male J?
U 1 1 622E5C1B
P 3850 3700
F 0 "J?" H 3958 3881 50  0000 C CNN
F 1 "STEPPER_3_PWR" H 3958 3790 50  0000 C CNN
F 2 "" H 3850 3700 50  0001 C CNN
F 3 "~" H 3850 3700 50  0001 C CNN
	1    3850 3700
	1    0    0    -1  
$EndComp
$EndSCHEMATC
