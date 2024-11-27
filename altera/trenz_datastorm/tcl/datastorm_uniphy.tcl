# qsys scripting (.tcl) file for datastorm_uniphy
package require qsys

create_system {datastorm_uniphy}

set_project_property DEVICE_FAMILY {Cyclone V}
set_project_property DEVICE {5CSEMA5F31C8}
set_project_property HIDE_FROM_IP_CATALOG {false}

# Instances and instance parameters
# (disabled instances are intentionally culled)
add_instance axi_bridge_0 altera_axi_bridge
set_instance_parameter_value axi_bridge_0 {ADDR_WIDTH} {32}
set_instance_parameter_value axi_bridge_0 {AXI_VERSION} {AXI4}
set_instance_parameter_value axi_bridge_0 {COMBINED_ACCEPTANCE_CAPABILITY} {16}
set_instance_parameter_value axi_bridge_0 {COMBINED_ISSUING_CAPABILITY} {16}
set_instance_parameter_value axi_bridge_0 {DATA_WIDTH} {64}
set_instance_parameter_value axi_bridge_0 {M0_ID_WIDTH} {4}
set_instance_parameter_value axi_bridge_0 {READ_ACCEPTANCE_CAPABILITY} {16}
set_instance_parameter_value axi_bridge_0 {READ_ADDR_USER_WIDTH} {64}
set_instance_parameter_value axi_bridge_0 {READ_DATA_REORDERING_DEPTH} {1}
set_instance_parameter_value axi_bridge_0 {READ_DATA_USER_WIDTH} {64}
set_instance_parameter_value axi_bridge_0 {READ_ISSUING_CAPABILITY} {16}
set_instance_parameter_value axi_bridge_0 {S0_ID_WIDTH} {4}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARBURST} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARCACHE} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARID} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARLEN} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARLOCK} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARQOS} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARREGION} {0}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARSIZE} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_ARUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWBURST} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWCACHE} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWID} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWLEN} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWLOCK} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWQOS} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWREGION} {0}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWSIZE} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_AWUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_M0_BID} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_BRESP} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_BUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_M0_RID} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_RLAST} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_RRESP} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_RUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_M0_WSTRB} {1}
set_instance_parameter_value axi_bridge_0 {USE_M0_WUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_PIPELINE} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_ARCACHE} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_ARLOCK} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_ARPROT} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_ARQOS} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_ARREGION} {0}
set_instance_parameter_value axi_bridge_0 {USE_S0_ARUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_S0_AWCACHE} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_AWLOCK} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_AWPROT} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_AWQOS} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_AWREGION} {0}
set_instance_parameter_value axi_bridge_0 {USE_S0_AWUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_S0_BRESP} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_BUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_S0_RRESP} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_RUSER} {0}
set_instance_parameter_value axi_bridge_0 {USE_S0_WLAST} {1}
set_instance_parameter_value axi_bridge_0 {USE_S0_WUSER} {0}
set_instance_parameter_value axi_bridge_0 {WRITE_ACCEPTANCE_CAPABILITY} {16}
set_instance_parameter_value axi_bridge_0 {WRITE_ADDR_USER_WIDTH} {64}
set_instance_parameter_value axi_bridge_0 {WRITE_DATA_USER_WIDTH} {64}
set_instance_parameter_value axi_bridge_0 {WRITE_ISSUING_CAPABILITY} {16}
set_instance_parameter_value axi_bridge_0 {WRITE_RESP_USER_WIDTH} {64}

add_instance clk_0 clock_source
set_instance_parameter_value clk_0 {clockFrequency} {40000000.0}
set_instance_parameter_value clk_0 {clockFrequencyKnown} {1}
set_instance_parameter_value clk_0 {resetSynchronousEdges} {NONE}

add_instance ddr_fpga altera_mem_if_ddr3_emif
set_instance_parameter_value ddr_fpga {ABSTRACT_REAL_COMPARE_TEST} {0}
set_instance_parameter_value ddr_fpga {ABS_RAM_MEM_INIT_FILENAME} {meminit}
set_instance_parameter_value ddr_fpga {ACV_PHY_CLK_ADD_FR_PHASE} {0.0}
set_instance_parameter_value ddr_fpga {AC_PACKAGE_DESKEW} {0}
set_instance_parameter_value ddr_fpga {AC_ROM_USER_ADD_0} {0_0000_0000_0000}
set_instance_parameter_value ddr_fpga {AC_ROM_USER_ADD_1} {0_0000_0000_1000}
set_instance_parameter_value ddr_fpga {ADDR_ORDER} {0}
set_instance_parameter_value ddr_fpga {ADD_EFFICIENCY_MONITOR} {0}
set_instance_parameter_value ddr_fpga {ADD_EXTERNAL_SEQ_DEBUG_NIOS} {0}
set_instance_parameter_value ddr_fpga {ADVANCED_CK_PHASES} {0}
set_instance_parameter_value ddr_fpga {ADVERTIZE_SEQUENCER_SW_BUILD_FILES} {0}
set_instance_parameter_value ddr_fpga {AFI_DEBUG_INFO_WIDTH} {32}
set_instance_parameter_value ddr_fpga {ALTMEMPHY_COMPATIBLE_MODE} {0}
set_instance_parameter_value ddr_fpga {AP_MODE} {0}
set_instance_parameter_value ddr_fpga {AP_MODE_EN} {0}
set_instance_parameter_value ddr_fpga {AUTO_PD_CYCLES} {0}
set_instance_parameter_value ddr_fpga {AUTO_POWERDN_EN} {0}
set_instance_parameter_value ddr_fpga {AVL_DATA_WIDTH_PORT} {32 32 32 32 32 32}
set_instance_parameter_value ddr_fpga {AVL_MAX_SIZE} {4}
set_instance_parameter_value ddr_fpga {BYTE_ENABLE} {1}
set_instance_parameter_value ddr_fpga {C2P_WRITE_CLOCK_ADD_PHASE} {0.0}
set_instance_parameter_value ddr_fpga {CALIBRATION_MODE} {Skip}
set_instance_parameter_value ddr_fpga {CALIB_REG_WIDTH} {8}
set_instance_parameter_value ddr_fpga {CFG_DATA_REORDERING_TYPE} {INTER_BANK}
set_instance_parameter_value ddr_fpga {CFG_REORDER_DATA} {1}
set_instance_parameter_value ddr_fpga {CFG_TCCD_NS} {2.5}
set_instance_parameter_value ddr_fpga {COMMAND_PHASE} {0.0}
set_instance_parameter_value ddr_fpga {CONTROLLER_LATENCY} {5}
set_instance_parameter_value ddr_fpga {CORE_DEBUG_CONNECTION} {EXPORT}
set_instance_parameter_value ddr_fpga {CPORT_TYPE_PORT} {Bidirectional Bidirectional Bidirectional Bidirectional Bidirectional Bidirectional}
set_instance_parameter_value ddr_fpga {CTL_AUTOPCH_EN} {0}
set_instance_parameter_value ddr_fpga {CTL_CMD_QUEUE_DEPTH} {8}
set_instance_parameter_value ddr_fpga {CTL_CSR_CONNECTION} {INTERNAL_JTAG}
set_instance_parameter_value ddr_fpga {CTL_CSR_ENABLED} {0}
set_instance_parameter_value ddr_fpga {CTL_CSR_READ_ONLY} {1}
set_instance_parameter_value ddr_fpga {CTL_DEEP_POWERDN_EN} {0}
set_instance_parameter_value ddr_fpga {CTL_DYNAMIC_BANK_ALLOCATION} {0}
set_instance_parameter_value ddr_fpga {CTL_DYNAMIC_BANK_NUM} {4}
set_instance_parameter_value ddr_fpga {CTL_ECC_AUTO_CORRECTION_ENABLED} {0}
set_instance_parameter_value ddr_fpga {CTL_ECC_ENABLED} {0}
set_instance_parameter_value ddr_fpga {CTL_ENABLE_BURST_INTERRUPT} {0}
set_instance_parameter_value ddr_fpga {CTL_ENABLE_BURST_TERMINATE} {0}
set_instance_parameter_value ddr_fpga {CTL_HRB_ENABLED} {0}
set_instance_parameter_value ddr_fpga {CTL_LOOK_AHEAD_DEPTH} {4}
set_instance_parameter_value ddr_fpga {CTL_SELF_REFRESH_EN} {0}
set_instance_parameter_value ddr_fpga {CTL_USR_REFRESH_EN} {0}
set_instance_parameter_value ddr_fpga {CTL_ZQCAL_EN} {0}
set_instance_parameter_value ddr_fpga {CUT_NEW_FAMILY_TIMING} {1}
set_instance_parameter_value ddr_fpga {DAT_DATA_WIDTH} {32}
set_instance_parameter_value ddr_fpga {DEBUG_MODE} {0}
set_instance_parameter_value ddr_fpga {DEVICE_DEPTH} {1}
set_instance_parameter_value ddr_fpga {DEVICE_FAMILY_PARAM} {}
set_instance_parameter_value ddr_fpga {DISABLE_CHILD_MESSAGING} {0}
set_instance_parameter_value ddr_fpga {DISCRETE_FLY_BY} {1}
set_instance_parameter_value ddr_fpga {DLL_SHARING_MODE} {None}
set_instance_parameter_value ddr_fpga {DQS_DQSN_MODE} {DIFFERENTIAL}
set_instance_parameter_value ddr_fpga {DQ_INPUT_REG_USE_CLKN} {0}
set_instance_parameter_value ddr_fpga {DUPLICATE_AC} {0}
set_instance_parameter_value ddr_fpga {ED_EXPORT_SEQ_DEBUG} {0}
set_instance_parameter_value ddr_fpga {ENABLE_ABS_RAM_MEM_INIT} {0}
set_instance_parameter_value ddr_fpga {ENABLE_BONDING} {0}
set_instance_parameter_value ddr_fpga {ENABLE_BURST_MERGE} {0}
set_instance_parameter_value ddr_fpga {ENABLE_CTRL_AVALON_INTERFACE} {1}
set_instance_parameter_value ddr_fpga {ENABLE_DELAY_CHAIN_WRITE} {0}
set_instance_parameter_value ddr_fpga {ENABLE_EMIT_BFM_MASTER} {0}
set_instance_parameter_value ddr_fpga {ENABLE_EXPORT_SEQ_DEBUG_BRIDGE} {0}
set_instance_parameter_value ddr_fpga {ENABLE_EXTRA_REPORTING} {0}
set_instance_parameter_value ddr_fpga {ENABLE_ISS_PROBES} {0}
set_instance_parameter_value ddr_fpga {ENABLE_NON_DESTRUCTIVE_CALIB} {0}
set_instance_parameter_value ddr_fpga {ENABLE_NON_DES_CAL} {0}
set_instance_parameter_value ddr_fpga {ENABLE_NON_DES_CAL_TEST} {0}
set_instance_parameter_value ddr_fpga {ENABLE_SEQUENCER_MARGINING_ON_BY_DEFAULT} {0}
set_instance_parameter_value ddr_fpga {ENABLE_USER_ECC} {0}
set_instance_parameter_value ddr_fpga {EXPORT_AFI_HALF_CLK} {0}
set_instance_parameter_value ddr_fpga {EXTRA_SETTINGS} {}
set_instance_parameter_value ddr_fpga {FIX_READ_LATENCY} {8}
set_instance_parameter_value ddr_fpga {FORCED_NON_LDC_ADDR_CMD_MEM_CK_INVERT} {0}
set_instance_parameter_value ddr_fpga {FORCED_NUM_WRITE_FR_CYCLE_SHIFTS} {0}
set_instance_parameter_value ddr_fpga {FORCE_DQS_TRACKING} {AUTO}
set_instance_parameter_value ddr_fpga {FORCE_MAX_LATENCY_COUNT_WIDTH} {0}
set_instance_parameter_value ddr_fpga {FORCE_SEQUENCER_TCL_DEBUG_MODE} {0}
set_instance_parameter_value ddr_fpga {FORCE_SHADOW_REGS} {AUTO}
set_instance_parameter_value ddr_fpga {FORCE_SYNTHESIS_LANGUAGE} {}
set_instance_parameter_value ddr_fpga {HARD_EMIF} {1}
set_instance_parameter_value ddr_fpga {HCX_COMPAT_MODE} {0}
set_instance_parameter_value ddr_fpga {HHP_HPS} {0}
set_instance_parameter_value ddr_fpga {HHP_HPS_SIMULATION} {0}
set_instance_parameter_value ddr_fpga {HHP_HPS_VERIFICATION} {0}
set_instance_parameter_value ddr_fpga {HPS_PROTOCOL} {DEFAULT}
set_instance_parameter_value ddr_fpga {INCLUDE_BOARD_DELAY_MODEL} {0}
set_instance_parameter_value ddr_fpga {INCLUDE_MULTIRANK_BOARD_DELAY_MODEL} {0}
set_instance_parameter_value ddr_fpga {IS_ES_DEVICE} {0}
set_instance_parameter_value ddr_fpga {LOCAL_ID_WIDTH} {8}
set_instance_parameter_value ddr_fpga {LRDIMM_EXTENDED_CONFIG} {0x000000000000000000}
set_instance_parameter_value ddr_fpga {MARGIN_VARIATION_TEST} {0}
set_instance_parameter_value ddr_fpga {MAX_PENDING_RD_CMD} {32}
set_instance_parameter_value ddr_fpga {MAX_PENDING_WR_CMD} {16}
set_instance_parameter_value ddr_fpga {MEM_ASR} {Manual}
set_instance_parameter_value ddr_fpga {MEM_ATCL} {Disabled}
set_instance_parameter_value ddr_fpga {MEM_AUTO_LEVELING_MODE} {1}
set_instance_parameter_value ddr_fpga {MEM_BANKADDR_WIDTH} {3}
set_instance_parameter_value ddr_fpga {MEM_BL} {OTF}
set_instance_parameter_value ddr_fpga {MEM_BT} {Sequential}
set_instance_parameter_value ddr_fpga {MEM_CK_PHASE} {0.0}
set_instance_parameter_value ddr_fpga {MEM_CK_WIDTH} {1}
set_instance_parameter_value ddr_fpga {MEM_CLK_EN_WIDTH} {1}
set_instance_parameter_value ddr_fpga {MEM_CLK_FREQ} {400.0}
set_instance_parameter_value ddr_fpga {MEM_CLK_FREQ_MAX} {400.0}
set_instance_parameter_value ddr_fpga {MEM_COL_ADDR_WIDTH} {10}
set_instance_parameter_value ddr_fpga {MEM_CS_WIDTH} {1}
set_instance_parameter_value ddr_fpga {MEM_DEVICE} {MISSING_MODEL}
set_instance_parameter_value ddr_fpga {MEM_DLL_EN} {1}
set_instance_parameter_value ddr_fpga {MEM_DQ_PER_DQS} {8}
set_instance_parameter_value ddr_fpga {MEM_DQ_WIDTH} {32}
set_instance_parameter_value ddr_fpga {MEM_DRV_STR} {RZQ/6}
set_instance_parameter_value ddr_fpga {MEM_FORMAT} {DISCRETE}
set_instance_parameter_value ddr_fpga {MEM_GUARANTEED_WRITE_INIT} {0}
set_instance_parameter_value ddr_fpga {MEM_IF_BOARD_BASE_DELAY} {10}
set_instance_parameter_value ddr_fpga {MEM_IF_DM_PINS_EN} {1}
set_instance_parameter_value ddr_fpga {MEM_IF_DQSN_EN} {1}
set_instance_parameter_value ddr_fpga {MEM_IF_SIM_VALID_WINDOW} {0}
set_instance_parameter_value ddr_fpga {MEM_INIT_EN} {0}
set_instance_parameter_value ddr_fpga {MEM_INIT_FILE} {}
set_instance_parameter_value ddr_fpga {MEM_MIRROR_ADDRESSING} {0}
set_instance_parameter_value ddr_fpga {MEM_NUMBER_OF_DIMMS} {1}
set_instance_parameter_value ddr_fpga {MEM_NUMBER_OF_RANKS_PER_DEVICE} {1}
set_instance_parameter_value ddr_fpga {MEM_NUMBER_OF_RANKS_PER_DIMM} {1}
set_instance_parameter_value ddr_fpga {MEM_PD} {DLL off}
set_instance_parameter_value ddr_fpga {MEM_RANK_MULTIPLICATION_FACTOR} {1}
set_instance_parameter_value ddr_fpga {MEM_ROW_ADDR_WIDTH} {15}
set_instance_parameter_value ddr_fpga {MEM_RTT_NOM} {RZQ/6}
set_instance_parameter_value ddr_fpga {MEM_RTT_WR} {Dynamic ODT off}
set_instance_parameter_value ddr_fpga {MEM_SRT} {Normal}
set_instance_parameter_value ddr_fpga {MEM_TCL} {7}
set_instance_parameter_value ddr_fpga {MEM_TFAW_NS} {30.0}
set_instance_parameter_value ddr_fpga {MEM_TINIT_US} {500}
set_instance_parameter_value ddr_fpga {MEM_TMRD_CK} {4}
set_instance_parameter_value ddr_fpga {MEM_TRAS_NS} {35.0}
set_instance_parameter_value ddr_fpga {MEM_TRCD_NS} {13.75}
set_instance_parameter_value ddr_fpga {MEM_TREFI_US} {7.8}
set_instance_parameter_value ddr_fpga {MEM_TRFC_NS} {260.0}
set_instance_parameter_value ddr_fpga {MEM_TRP_NS} {13.75}
set_instance_parameter_value ddr_fpga {MEM_TRRD_NS} {10.0}
set_instance_parameter_value ddr_fpga {MEM_TRTP_NS} {10.0}
set_instance_parameter_value ddr_fpga {MEM_TWR_NS} {15.0}
set_instance_parameter_value ddr_fpga {MEM_TWTR} {4}
set_instance_parameter_value ddr_fpga {MEM_USER_LEVELING_MODE} {Leveling}
set_instance_parameter_value ddr_fpga {MEM_VENDOR} {JEDEC}
set_instance_parameter_value ddr_fpga {MEM_VERBOSE} {1}
set_instance_parameter_value ddr_fpga {MEM_VOLTAGE} {1.5V DDR3}
set_instance_parameter_value ddr_fpga {MEM_WTCL} {6}
set_instance_parameter_value ddr_fpga {MRS_MIRROR_PING_PONG_ATSO} {0}
set_instance_parameter_value ddr_fpga {MULTICAST_EN} {0}
set_instance_parameter_value ddr_fpga {NEXTGEN} {1}
set_instance_parameter_value ddr_fpga {NIOS_ROM_DATA_WIDTH} {32}
set_instance_parameter_value ddr_fpga {NUM_DLL_SHARING_INTERFACES} {1}
set_instance_parameter_value ddr_fpga {NUM_EXTRA_REPORT_PATH} {10}
set_instance_parameter_value ddr_fpga {NUM_OCT_SHARING_INTERFACES} {1}
set_instance_parameter_value ddr_fpga {NUM_OF_PORTS} {1}
set_instance_parameter_value ddr_fpga {NUM_PLL_SHARING_INTERFACES} {1}
set_instance_parameter_value ddr_fpga {OCT_SHARING_MODE} {None}
set_instance_parameter_value ddr_fpga {P2C_READ_CLOCK_ADD_PHASE} {0.0}
set_instance_parameter_value ddr_fpga {PACKAGE_DESKEW} {0}
set_instance_parameter_value ddr_fpga {PARSE_FRIENDLY_DEVICE_FAMILY_PARAM} {}
set_instance_parameter_value ddr_fpga {PARSE_FRIENDLY_DEVICE_FAMILY_PARAM_VALID} {0}
set_instance_parameter_value ddr_fpga {PHY_CSR_CONNECTION} {INTERNAL_JTAG}
set_instance_parameter_value ddr_fpga {PHY_CSR_ENABLED} {0}
set_instance_parameter_value ddr_fpga {PHY_ONLY} {0}
set_instance_parameter_value ddr_fpga {PINGPONGPHY_EN} {0}
set_instance_parameter_value ddr_fpga {PLL_ADDR_CMD_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_ADDR_CMD_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_ADDR_CMD_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_ADDR_CMD_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_ADDR_CMD_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_ADDR_CMD_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_AFI_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_AFI_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_AFI_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_AFI_HALF_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_HALF_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_AFI_HALF_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_AFI_HALF_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_HALF_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_HALF_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_AFI_PHY_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_PHY_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_AFI_PHY_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_AFI_PHY_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_PHY_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_AFI_PHY_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_C2P_WRITE_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_C2P_WRITE_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_C2P_WRITE_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_C2P_WRITE_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_C2P_WRITE_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_C2P_WRITE_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_CLK_PARAM_VALID} {0}
set_instance_parameter_value ddr_fpga {PLL_CONFIG_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_CONFIG_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_CONFIG_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_CONFIG_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_CONFIG_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_CONFIG_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_DR_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_DR_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_DR_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_DR_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_DR_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_DR_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_HR_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_HR_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_HR_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_HR_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_HR_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_HR_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_LOCATION} {Top_Bottom}
set_instance_parameter_value ddr_fpga {PLL_MEM_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_MEM_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_MEM_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_MEM_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_MEM_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_MEM_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_NIOS_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_NIOS_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_NIOS_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_NIOS_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_NIOS_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_NIOS_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_P2C_READ_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_P2C_READ_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_P2C_READ_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_P2C_READ_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_P2C_READ_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_P2C_READ_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_SHARING_MODE} {None}
set_instance_parameter_value ddr_fpga {PLL_WRITE_CLK_DIV_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_WRITE_CLK_FREQ_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {PLL_WRITE_CLK_FREQ_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {PLL_WRITE_CLK_MULT_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_WRITE_CLK_PHASE_PS_PARAM} {0}
set_instance_parameter_value ddr_fpga {PLL_WRITE_CLK_PHASE_PS_SIM_STR_PARAM} {}
set_instance_parameter_value ddr_fpga {POWER_OF_TWO_BUS} {0}
set_instance_parameter_value ddr_fpga {PRIORITY_PORT} {1 1 1 1 1 1}
set_instance_parameter_value ddr_fpga {RATE} {Full}
set_instance_parameter_value ddr_fpga {RDIMM_CONFIG} {0000000000000000}
set_instance_parameter_value ddr_fpga {READ_DQ_DQS_CLOCK_SOURCE} {INVERTED_DQS_BUS}
set_instance_parameter_value ddr_fpga {READ_FIFO_SIZE} {8}
set_instance_parameter_value ddr_fpga {REFRESH_BURST_VALIDATION} {0}
set_instance_parameter_value ddr_fpga {REFRESH_INTERVAL} {15000}
set_instance_parameter_value ddr_fpga {REF_CLK_FREQ} {50.0}
set_instance_parameter_value ddr_fpga {REF_CLK_FREQ_MAX_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {REF_CLK_FREQ_MIN_PARAM} {0.0}
set_instance_parameter_value ddr_fpga {REF_CLK_FREQ_PARAM_VALID} {0}
set_instance_parameter_value ddr_fpga {SEQUENCER_TYPE} {NIOS}
set_instance_parameter_value ddr_fpga {SEQ_MODE} {0}
set_instance_parameter_value ddr_fpga {SKIP_MEM_INIT} {1}
set_instance_parameter_value ddr_fpga {SOPC_COMPAT_RESET} {0}
set_instance_parameter_value ddr_fpga {SPEED_GRADE} {6}
set_instance_parameter_value ddr_fpga {STARVE_LIMIT} {10}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_AC_EYE_REDUCTION_H} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_AC_EYE_REDUCTION_SU} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_AC_SKEW} {0.02}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_AC_SLEW_RATE} {1.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_AC_TO_CK_SKEW} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_CK_CKN_SLEW_RATE} {2.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DELTA_DQS_ARRIVAL_TIME} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DELTA_READ_DQS_ARRIVAL_TIME} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DERATE_METHOD} {AUTO}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DQS_DQSN_SLEW_RATE} {2.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DQ_EYE_REDUCTION} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DQ_SLEW_RATE} {1.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_DQ_TO_DQS_SKEW} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_ISI_METHOD} {AUTO}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_MAX_CK_DELAY} {0.6}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_MAX_DQS_DELAY} {0.6}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_READ_DQ_EYE_REDUCTION} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_SKEW_BETWEEN_DIMMS} {0.05}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_SKEW_BETWEEN_DQS} {0.02}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_SKEW_CKDQS_DIMM_MAX} {0.01}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_SKEW_CKDQS_DIMM_MIN} {-0.01}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_SKEW_WITHIN_DQS} {0.02}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_TDH} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_TDS} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_TIH} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_BOARD_TIS} {0.0}
set_instance_parameter_value ddr_fpga {TIMING_TDH} {65}
set_instance_parameter_value ddr_fpga {TIMING_TDQSCK} {255}
set_instance_parameter_value ddr_fpga {TIMING_TDQSCKDL} {1200}
set_instance_parameter_value ddr_fpga {TIMING_TDQSCKDM} {900}
set_instance_parameter_value ddr_fpga {TIMING_TDQSCKDS} {450}
set_instance_parameter_value ddr_fpga {TIMING_TDQSQ} {125}
set_instance_parameter_value ddr_fpga {TIMING_TDQSS} {0.25}
set_instance_parameter_value ddr_fpga {TIMING_TDS} {30}
set_instance_parameter_value ddr_fpga {TIMING_TDSH} {0.2}
set_instance_parameter_value ddr_fpga {TIMING_TDSS} {0.2}
set_instance_parameter_value ddr_fpga {TIMING_TIH} {140}
set_instance_parameter_value ddr_fpga {TIMING_TIS} {180}
set_instance_parameter_value ddr_fpga {TIMING_TQH} {0.38}
set_instance_parameter_value ddr_fpga {TIMING_TQSH} {0.4}
set_instance_parameter_value ddr_fpga {TRACKING_ERROR_TEST} {0}
set_instance_parameter_value ddr_fpga {TRACKING_WATCH_TEST} {0}
set_instance_parameter_value ddr_fpga {TREFI} {35100}
set_instance_parameter_value ddr_fpga {TRFC} {350}
set_instance_parameter_value ddr_fpga {USER_DEBUG_LEVEL} {1}
set_instance_parameter_value ddr_fpga {USE_AXI_ADAPTOR} {0}
set_instance_parameter_value ddr_fpga {USE_FAKE_PHY} {0}
set_instance_parameter_value ddr_fpga {USE_MEM_CLK_FREQ} {0}
set_instance_parameter_value ddr_fpga {USE_MM_ADAPTOR} {1}
set_instance_parameter_value ddr_fpga {USE_SEQUENCER_BFM} {0}
set_instance_parameter_value ddr_fpga {WEIGHT_PORT} {0 0 0 0 0 0}
set_instance_parameter_value ddr_fpga {WRBUFFER_ADDR_WIDTH} {6}

# exported interfaces
add_interface clk clock sink
set_interface_property clk EXPORT_OF clk_0.clk_in
add_interface ddr3 conduit end
set_interface_property ddr3 EXPORT_OF ddr_fpga.memory
add_interface ddr_fpga_pll_ref_clk clock sink
set_interface_property ddr_fpga_pll_ref_clk EXPORT_OF ddr_fpga.pll_ref_clk
add_interface mem_status conduit end
set_interface_property mem_status EXPORT_OF ddr_fpga.status
add_interface oct conduit end
set_interface_property oct EXPORT_OF ddr_fpga.oct
add_interface reset reset sink
set_interface_property reset EXPORT_OF clk_0.clk_in_reset
add_interface s_axi altera_axi4 slave
set_interface_property s_axi EXPORT_OF axi_bridge_0.s0

# connections and connection parameters
add_connection axi_bridge_0.m0 ddr_fpga.avl_0
set_connection_parameter_value axi_bridge_0.m0/ddr_fpga.avl_0 arbitrationPriority {1}
set_connection_parameter_value axi_bridge_0.m0/ddr_fpga.avl_0 baseAddress {0x0000}
set_connection_parameter_value axi_bridge_0.m0/ddr_fpga.avl_0 defaultConnection {0}

add_connection clk_0.clk axi_bridge_0.clk

add_connection clk_0.clk ddr_fpga.mp_cmd_clk_0

add_connection clk_0.clk ddr_fpga.mp_rfifo_clk_0

add_connection clk_0.clk ddr_fpga.mp_wfifo_clk_0

add_connection clk_0.clk_reset axi_bridge_0.clk_reset

add_connection clk_0.clk_reset ddr_fpga.global_reset

add_connection clk_0.clk_reset ddr_fpga.mp_cmd_reset_n_0

add_connection clk_0.clk_reset ddr_fpga.mp_rfifo_reset_n_0

add_connection clk_0.clk_reset ddr_fpga.mp_wfifo_reset_n_0

add_connection clk_0.clk_reset ddr_fpga.soft_reset

# interconnect requirements
set_interconnect_requirement {$system} {qsys_mm.clockCrossingAdapter} {HANDSHAKE}
set_interconnect_requirement {$system} {qsys_mm.insertDefaultSlave} {FALSE}
set_interconnect_requirement {$system} {qsys_mm.maxAdditionalLatency} {3}
set_interconnect_requirement {fpga_only_master.master} {qsys_mm.security} {SECURE}

save_system {datastorm_uniphy.qsys}
