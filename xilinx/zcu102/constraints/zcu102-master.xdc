#-------------- MCS Generation ----------------------
#set_property BITSTREAM.CONFIG.EXTMASTERCCLK_EN div-1  [current_design]
#set_property BITSTREAM.CONFIG.SPI_FALL_EDGE YES       [current_design]
#set_property BITSTREAM.CONFIG.SPI_BUSWIDTH 8          [current_design]
#set_property BITSTREAM.GENERAL.COMPRESS TRUE          [current_design]
#set_property BITSTREAM.CONFIG.UNUSEDPIN Pulldown      [current_design]
#set_property CFGBVS GND                               [current_design]
#set_property CONFIG_VOLTAGE 1.8                       [current_design]
#set_property CONFIG_MODE SPIx8                        [current_design]

#---------------Physical Constraints-----------------

#THIS SHOULD BE ALRIGHT
#CLOCK USER_SI570 300MHZ

#set_property CLOCK_DEDICATED_ROUTE FALSE [get_nets sys_clk_ibufds/O]

set_property IOSTANDARD DIFF_SSTL12_DCI [get_ports sys_diff_clock_clk_p]

set_property PACKAGE_PIN AL8 [get_ports sys_diff_clock_clk_p]
set_property PACKAGE_PIN AL7 [get_ports sys_diff_clock_clk_n]
set_property IOSTANDARD DIFF_SSTL12_DCI [get_ports sys_diff_clock_clk_n]

set_property PACKAGE_PIN AM13 [get_ports reset]
set_property IOSTANDARD LVCMOS33 [get_ports reset]

create_clock -period 3.300 -name sys_diff_clk [get_ports sys_diff_clock_clk_p]

set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[8]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[9]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[10]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[11]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[12]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[13]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[14]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[15]}]

set_property IOSTANDARD DIFF_POD12_DCI     [get_ports {ddr_c0_ddr4_dqs_t[0]}]
set_property IOSTANDARD POD12_DCI     [get_ports {ddr_c0_ddr4_dqs_t[1]}]
set_property IOSTANDARD DIFF_POD12_DCI     [get_ports {ddr_c0_ddr4_dqs_t[2]}]

set_property IOSTANDARD DIFF_POD12_DCI  [get_ports {ddr_c0_ddr4_dqs_c[0]}]
set_property IOSTANDARD POD12_DCI  [get_ports {ddr_c0_ddr4_dqs_c[1]}]



set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[0]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[1]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[2]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[3]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[4]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[5]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[6]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[7]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[8]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[9]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[10]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[11]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[12]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[13]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_ba[0]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_ba[1]}]
set_property IOSTANDARD SSTL12_DCI [get_ports ddr_c0_ddr4_bg]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[14]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[16]}]
set_property IOSTANDARD SSTL12_DCI [get_ports {ddr_c0_ddr4_adr[15]}]

set_property IOSTANDARD DIFF_SSTL12_DCI [get_ports ddr_c0_ddr4_ck_t]
set_property IOSTANDARD DIFF_SSTL12_DCI [get_ports ddr_c0_ddr4_ck_c]

set_property IOSTANDARD SSTL12_DCI [get_ports ddr_c0_ddr4_cke]
set_property IOSTANDARD SSTL12_DCI [get_ports ddr_c0_ddr4_act_n]

set_property IOSTANDARD LVCMOS18 [get_ports ddr_c0_ddr4_reset_n]

set_property IOSTANDARD SSTL12_DCI [get_ports ddr_c0_ddr4_odt]
set_property IOSTANDARD SSTL12_DCI [get_ports ddr_c0_ddr4_cs_n]

set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[0]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[1]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[2]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[3]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[4]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[5]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[6]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dq[7]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dm_dbi_n[0]}]
set_property IOSTANDARD POD12_DCI [get_ports {ddr_c0_ddr4_dm_dbi_n[1]}]


set_input_jitter [get_clocks -of_objects [get_ports sys_diff_clock_clk_p]] 0.050

# DDR4 C0
set_property PACKAGE_PIN AK8 [get_ports ddr_c0_ddr4_act_n]
set_property PACKAGE_PIN AM8 [get_ports {ddr_c0_ddr4_adr[0]}]
set_property PACKAGE_PIN AM10 [get_ports {ddr_c0_ddr4_adr[10]}]
set_property PACKAGE_PIN AL10 [get_ports {ddr_c0_ddr4_adr[11]}]
set_property PACKAGE_PIN AM11 [get_ports {ddr_c0_ddr4_adr[12]}]
set_property PACKAGE_PIN AL11 [get_ports {ddr_c0_ddr4_adr[13]}]
set_property PACKAGE_PIN AJ7 [get_ports {ddr_c0_ddr4_adr[14]}]
set_property PACKAGE_PIN AL5 [get_ports {ddr_c0_ddr4_adr[15]}]
set_property PACKAGE_PIN AJ9 [get_ports {ddr_c0_ddr4_adr[16]}]
set_property PACKAGE_PIN AM9 [get_ports {ddr_c0_ddr4_adr[1]}]
set_property PACKAGE_PIN AP8 [get_ports {ddr_c0_ddr4_adr[2]}]
set_property PACKAGE_PIN AN8 [get_ports {ddr_c0_ddr4_adr[3]}]
set_property PACKAGE_PIN AK10 [get_ports {ddr_c0_ddr4_adr[4]}]
set_property PACKAGE_PIN AJ10 [get_ports {ddr_c0_ddr4_adr[5]}]
set_property PACKAGE_PIN AP9 [get_ports {ddr_c0_ddr4_adr[6]}]
set_property PACKAGE_PIN AN9 [get_ports {ddr_c0_ddr4_adr[7]}]
set_property PACKAGE_PIN AP10 [get_ports {ddr_c0_ddr4_adr[8]}]
set_property PACKAGE_PIN AP11 [get_ports {ddr_c0_ddr4_adr[9]}]
set_property PACKAGE_PIN AK12 [get_ports {ddr_c0_ddr4_ba[0]}]
set_property PACKAGE_PIN AJ12 [get_ports {ddr_c0_ddr4_ba[1]}]
set_property PACKAGE_PIN AK7 [get_ports ddr_c0_ddr4_bg]
set_property PACKAGE_PIN AN7 [get_ports ddr_c0_ddr4_ck_t]
set_property PACKAGE_PIN AP7 [get_ports ddr_c0_ddr4_ck_c]
set_property PACKAGE_PIN AM3 [get_ports ddr_c0_ddr4_cke]
set_property PACKAGE_PIN AP2 [get_ports ddr_c0_ddr4_cs_n]
set_property PACKAGE_PIN AL6 [get_ports {ddr_c0_ddr4_dm_dbi_n[0]}]
set_property PACKAGE_PIN AN2 [get_ports {ddr_c0_ddr4_dm_dbi_n[1]}]

# NOT FOUND
#set_property PACKAGE_PIN K17      [get_ports "ddr_c0_ddr4_dm_dbi_n[2]"]
#set_property PACKAGE_PIN G18      [get_ports "ddr_c0_ddr4_dm_dbi_n[3]"]
#set_property PACKAGE_PIN B18      [get_ports "ddr_c0_ddr4_dm_dbi_n[4]"]
#set_property PACKAGE_PIN P20      [get_ports "ddr_c0_ddr4_dm_dbi_n[5]"]
#set_property PACKAGE_PIN L23      [get_ports "ddr_c0_ddr4_dm_dbi_n[6]"]
#set_property PACKAGE_PIN G22      [get_ports "ddr_c0_ddr4_dm_dbi_n[7]"]

set_property PACKAGE_PIN AK4 [get_ports {ddr_c0_ddr4_dq[0]}]
set_property PACKAGE_PIN AL1 [get_ports {ddr_c0_ddr4_dq[10]}]
set_property PACKAGE_PIN AK1 [get_ports {ddr_c0_ddr4_dq[11]}]
set_property PACKAGE_PIN AN1 [get_ports {ddr_c0_ddr4_dq[12]}]
set_property PACKAGE_PIN AM1 [get_ports {ddr_c0_ddr4_dq[13]}]
set_property PACKAGE_PIN AP3 [get_ports {ddr_c0_ddr4_dq[14]}]
set_property PACKAGE_PIN AN3 [get_ports {ddr_c0_ddr4_dq[15]}]

# NOT USED
#set_property PACKAGE_PIN L16      [get_ports "ddr_c0_ddr4_dq[16]"]
#set_property PACKAGE_PIN K16      [get_ports "ddr_c0_ddr4_dq[17]"]
#set_property PACKAGE_PIN L18      [get_ports "ddr_c0_ddr4_dq[18]"]
#set_property PACKAGE_PIN K18      [get_ports "ddr_c0_ddr4_dq[19]"]

set_property PACKAGE_PIN AK5 [get_ports {ddr_c0_ddr4_dq[1]}]

#set_property PACKAGE_PIN J17      [get_ports "ddr_c0_ddr4_dq[20]"]
#set_property PACKAGE_PIN H17      [get_ports "ddr_c0_ddr4_dq[21]"]
#set_property PACKAGE_PIN H19      [get_ports "ddr_c0_ddr4_dq[22]"]
#set_property PACKAGE_PIN H18      [get_ports "ddr_c0_ddr4_dq[23]"]
#set_property PACKAGE_PIN F19      [get_ports "ddr_c0_ddr4_dq[24]"]
#set_property PACKAGE_PIN F18      [get_ports "ddr_c0_ddr4_dq[25]"]
#set_property PACKAGE_PIN E19      [get_ports "ddr_c0_ddr4_dq[26]"]
#set_property PACKAGE_PIN E18      [get_ports "ddr_c0_ddr4_dq[27]"]
#set_property PACKAGE_PIN G20      [get_ports "ddr_c0_ddr4_dq[28]"]
#set_property PACKAGE_PIN F20      [get_ports "ddr_c0_ddr4_dq[29]"]

set_property PACKAGE_PIN AN4 [get_ports {ddr_c0_ddr4_dq[2]}]

#set_property PACKAGE_PIN E17      [get_ports "ddr_c0_ddr4_dq[30]"]
#set_property PACKAGE_PIN D16      [get_ports "ddr_c0_ddr4_dq[31]"]
#set_property PACKAGE_PIN D17      [get_ports "ddr_c0_ddr4_dq[32]"]
#set_property PACKAGE_PIN C17      [get_ports "ddr_c0_ddr4_dq[33]"]
#set_property PACKAGE_PIN C19      [get_ports "ddr_c0_ddr4_dq[34]"]
#set_property PACKAGE_PIN C18      [get_ports "ddr_c0_ddr4_dq[35]"]
#set_property PACKAGE_PIN D20      [get_ports "ddr_c0_ddr4_dq[36]"]
#set_property PACKAGE_PIN D19      [get_ports "ddr_c0_ddr4_dq[37]"]
#set_property PACKAGE_PIN C20      [get_ports "ddr_c0_ddr4_dq[38]"]
#set_property PACKAGE_PIN B20      [get_ports "ddr_c0_ddr4_dq[39]"]

set_property PACKAGE_PIN AM4 [get_ports {ddr_c0_ddr4_dq[3]}]

#set_property PACKAGE_PIN N23      [get_ports "ddr_c0_ddr4_dq[40]"]
#set_property PACKAGE_PIN M23      [get_ports "ddr_c0_ddr4_dq[41]"]
#set_property PACKAGE_PIN R21      [get_ports "ddr_c0_ddr4_dq[42]"]
#set_property PACKAGE_PIN P21      [get_ports "ddr_c0_ddr4_dq[43]"]
#set_property PACKAGE_PIN R22      [get_ports "ddr_c0_ddr4_dq[44]"]
#set_property PACKAGE_PIN P22      [get_ports "ddr_c0_ddr4_dq[45]"]
#set_property PACKAGE_PIN T23      [get_ports "ddr_c0_ddr4_dq[46]"]
#set_property PACKAGE_PIN R23      [get_ports "ddr_c0_ddr4_dq[47]"]
#set_property PACKAGE_PIN K24      [get_ports "ddr_c0_ddr4_dq[48]"]
#set_property PACKAGE_PIN J24      [get_ports "ddr_c0_ddr4_dq[49]"]

set_property PACKAGE_PIN AP4 [get_ports {ddr_c0_ddr4_dq[4]}]

#set_property PACKAGE_PIN M21      [get_ports "ddr_c0_ddr4_dq[50]"]
#set_property PACKAGE_PIN L21      [get_ports "ddr_c0_ddr4_dq[51]"]
#set_property PACKAGE_PIN K21      [get_ports "ddr_c0_ddr4_dq[52]"]
#set_property PACKAGE_PIN J21      [get_ports "ddr_c0_ddr4_dq[53]"]
#set_property PACKAGE_PIN K22      [get_ports "ddr_c0_ddr4_dq[54]"]
#set_property PACKAGE_PIN J22      [get_ports "ddr_c0_ddr4_dq[55]"]
#set_property PACKAGE_PIN H23      [get_ports "ddr_c0_ddr4_dq[56]"]
#set_property PACKAGE_PIN H22      [get_ports "ddr_c0_ddr4_dq[57]"]
#set_property PACKAGE_PIN E23      [get_ports "ddr_c0_ddr4_dq[58]"]
#set_property PACKAGE_PIN E22      [get_ports "ddr_c0_ddr4_dq[59]"]

set_property PACKAGE_PIN AP5 [get_ports {ddr_c0_ddr4_dq[5]}]

#set_property PACKAGE_PIN F21      [get_ports "ddr_c0_ddr4_dq[60]"]
#set_property PACKAGE_PIN E21      [get_ports "ddr_c0_ddr4_dq[61]"]
#set_property PACKAGE_PIN F24      [get_ports "ddr_c0_ddr4_dq[62]"]
#set_property PACKAGE_PIN F23      [get_ports "ddr_c0_ddr4_dq[63]"]

set_property PACKAGE_PIN AM5 [get_ports {ddr_c0_ddr4_dq[6]}]
set_property PACKAGE_PIN AM6 [get_ports {ddr_c0_ddr4_dq[7]}]
set_property PACKAGE_PIN AK2 [get_ports {ddr_c0_ddr4_dq[8]}]
set_property PACKAGE_PIN AK3 [get_ports {ddr_c0_ddr4_dq[9]}]

#set_property PACKAGE_PIN AL2      [get_ports "ddr_c0_ddr4_dqs_c[2]"]
#set_property PACKAGE_PIN AL3      [get_ports "ddr_c0_ddr4_dqs_c[3]"]

#set_property PACKAGE_PIN A18      [get_ports "ddr_c0_ddr4_dqs_c[4]"]
#set_property PACKAGE_PIN M22      [get_ports "ddr_c0_ddr4_dqs_c[5]"]
#set_property PACKAGE_PIN L20      [get_ports "ddr_c0_ddr4_dqs_c[6]"]
#set_property PACKAGE_PIN G23      [get_ports "ddr_c0_ddr4_dqs_c[7]"]
set_property PACKAGE_PIN AN6       [get_ports {ddr_c0_ddr4_dqs_t[0]}]
set_property PACKAGE_PIN AP6       [get_ports {ddr_c0_ddr4_dqs_c[0]}]
set_property PACKAGE_PIN AL3       [get_ports {ddr_c0_ddr4_dqs_t[1]}]
set_property PACKAGE_PIN AL2       [get_ports {ddr_c0_ddr4_dqs_c[1]}]
#set_property PACKAGE_PIN AL3      [get_ports "ddr_c0_ddr4_dqs_t[2]"]
#set_property PACKAGE_PIN F16      [get_ports "ddr_c0_ddr4_dqs_t[3]"]
#set_property PACKAGE_PIN A19      [get_ports "ddr_c0_ddr4_dqs_t[4]"]
#set_property PACKAGE_PIN N22      [get_ports "ddr_c0_ddr4_dqs_t[5]"]
#set_property PACKAGE_PIN M20      [get_ports "ddr_c0_ddr4_dqs_t[6]"]
#set_property PACKAGE_PIN H24      [get_ports "ddr_c0_ddr4_dqs_t[7]"]

set_property PACKAGE_PIN AK9 [get_ports ddr_c0_ddr4_odt]

set_property PACKAGE_PIN AH9 [get_ports ddr_c0_ddr4_reset_n]


# NOT SURE
set_property PACKAGE_PIN E12 [get_ports uart_ctsn]
set_property IOSTANDARD LVCMOS33 [get_ports uart_ctsn]
#set_property IOB TRUE [get_ports uart_ctsn]
set_property PACKAGE_PIN F13 [get_ports uart_tx]
set_property IOSTANDARD LVCMOS33 [get_ports uart_tx]
#set_property IOB TRUE [get_ports uart_tx]
set_property PACKAGE_PIN D12 [get_ports uart_rtsn]
set_property IOSTANDARD LVCMOS33 [get_ports uart_rtsn]
#set_property IOB TRUE [get_ports uart_rtsn]
set_property PACKAGE_PIN E13 [get_ports uart_rx]
set_property IOSTANDARD LVCMOS33 [get_ports uart_rx]
#set_property IOB TRUE [get_ports uart_rx]

# Platform specific constraints

# JTAG -- CAN'T FOUND
set_property CLOCK_DEDICATED_ROUTE FALSE [get_nets jtag_TCK_IBUF_inst/O]
set_property PACKAGE_PIN D20 [get_ports jtag_TCK]
set_property IOSTANDARD LVCMOS33 [get_ports jtag_TCK]
set_property PACKAGE_PIN E20 [get_ports jtag_TMS]
set_property IOSTANDARD LVCMOS33 [get_ports jtag_TMS]
set_property PACKAGE_PIN D22 [get_ports jtag_TDI]
set_property IOSTANDARD LVCMOS33 [get_ports jtag_TDI]
set_property PACKAGE_PIN E22 [get_ports jtag_TDO]
set_property IOSTANDARD LVCMOS33 [get_ports jtag_TDO]

# SDIO
set_property PACKAGE_PIN A21 [get_ports sdio_clk]
set_property IOSTANDARD LVCMOS33 [get_ports sdio_clk]
set_property PACKAGE_PIN B20 [get_ports sdio_cmd]
set_property IOSTANDARD LVCMOS33 [get_ports sdio_cmd]
set_property PACKAGE_PIN A22 [get_ports {sdio_dat[0]}]
set_property IOSTANDARD LVCMOS33 [get_ports {sdio_dat[0]}]
set_property PACKAGE_PIN B21 [get_ports {sdio_dat[1]}]
set_property IOSTANDARD LVCMOS33 [get_ports {sdio_dat[1]}]
set_property PACKAGE_PIN C21 [get_ports {sdio_dat[2]}]
set_property IOSTANDARD LVCMOS33 [get_ports {sdio_dat[2]}]
set_property PACKAGE_PIN A20 [get_ports {sdio_dat[3]}]
set_property IOSTANDARD LVCMOS33 [get_ports {sdio_dat[3]}]


set_property BITSTREAM.GENERAL.COMPRESS TRUE [current_design]
set_property BITSTREAM.CONFIG.UNUSEDPIN Pulldown [current_design]
set_property CONFIG_VOLTAGE 1.8 [current_design]



