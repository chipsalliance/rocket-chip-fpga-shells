package require ::quartus::flow

set top_model [lindex $argv 0]
set board [lindex $argv 1]
set proj_longname [lindex $argv 2]

set ip_tcls [lindex $argv 3]

project_new -overwrite -revision $top_model $top_model
# Setup board specific quartus settings
set scriptdir [file dirname [info script]]
set boarddir [file join [file dirname [file dirname $scriptdir]] $board]

source [file join $boarddir tcl board.tcl]

# Setup common quartus lanugage settings
set_global_assignment -name PROJECT_OUTPUT_DIRECTORY output_files
set_global_assignment -name PARTITION_NETLIST_TYPE SOURCE -section_id Top
set_global_assignment -name PARTITION_FITTER_PRESERVATION_LEVEL PLACEMENT_AND_ROUTING -section_id Top
set_global_assignment -name PARTITION_COLOR 16764057 -section_id Top
set_global_assignment -name VERILOG_INPUT_VERSION SYSTEMVERILOG_2005
set_global_assignment -name VERILOG_SHOW_LMF_MAPPING_MESSAGES OFF
set_global_assignment -name VERILOG_MACRO "SYNTHESIS=<None>"

# Generate and add all IP
if {$ip_tcls ne {}} {

	set ip_tcls [regexp -inline -all -- {\S+} $ip_tcls]

	foreach ip_tcl $ip_tcls {
		source $ip_tcl
	}
}

# Add synthesis fileset to quartus
set sl [open $proj_longname.vsrcs.f r]
set files [lsearch -not -exact -all -inline [split [read $sl] "\n"] {}]

foreach path $files {
	if {![string match {#*} $path]} {
		if {[string match {*.v} $path]} {
			set_global_assignment -name VERILOG_FILE $path
		} elseif {[string match {*.sv} $path]} {
			set_global_assignment -name SYSTEMVERILOG_FILE $path
		}
	}
}

# Add pin assignments and clock constraints
source $proj_longname.assign.tcl
set_global_assignment -name SDC_FILE $proj_longname.shell.sdc

export_assignments

execute_flow -compile
project_close

