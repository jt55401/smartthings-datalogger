/**
 *  Data Logger
 *
 *  Author: jgrey
 *  Date: 2015-09-01
 *  
 *  Log data to a HTTP POST Endpoint.
 *  Works as-is with fluentd (uses a single json parameter in the body of the POST).
 *  Can easily modifed to suit your needs with other systems.
 *
 *
 *  Copyright 2014 Jason Grey
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
 
definition(
	name: "SmartThings Data Logger",
	namespace: "jt55401",
	author: "jt55401",
	description: "Log to fluentd",
	category: "My Apps",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
	iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	section("Log devices...") {
		input "temperatures", "capability.temperatureMeasurement", title: "Temperatures", required:false, multiple: true
		input "contacts", "capability.contactSensor", title: "Contacts", required: false, multiple: true
		input "accelerations", "capability.accelerationSensor", title: "Accelerations", required: false, multiple: true
		input "motions", "capability.motionSensor", title: "Motions", required: false, multiple: true
		input "switches", "capability.switch", title: "Switches", required: false, multiple: true
		input "illuminaces", "capability.illuminanceMeasurement", title: "Light Sensors", required: false, multiple: true
		input "humidities", "capability.relativeHumidityMeasurement", title: "Humidity", required: false, multiple: true
		input "energymeters", "capability.energyMeter", title: "Energy", required: false, multiple: true
		input "powermeters", "capability.powerMeter", title: "Power", required: false, multiple: true
		input "batteries", "capability.battery", title: "Battery", required: false, multiple: true
	}
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(temperatures, "temperature", handleTemperatureEvent)
	subscribe(contacts, "contact", handleContactEvent)
	subscribe(accelerations, "acceleration", handleAccelerationEvent)
	subscribe(motions, "motion", handleMotionEvent)
	subscribe(switches, "switch", handleSwitchEvent)
	subscribe(illuminaces, "illuminance", handleLightEvent)
	subscribe(humidities, "humidity", handleHumidityEvent)
	subscribe(energymeters, "energy", handleEnergyEvent)
	subscribe(powermeters, "power", handlePowerEvent)
	subscribe(batteries, "battery", handleBatteryEvent)
	subscribe(location, null, lanResponseHandler, [filterEvents:false])
}

def handleTemperatureEvent(evt) {
	logField(evt,"temperature", evt.value.toString())
}

def handleContactEvent(evt) {
	logField(evt,"contact", evt.value.toString() )
}

def handleAccelerationEvent(evt) {
	logField(evt,"acceleration",evt.value.toString())
}

def handleMotionEvent(evt) {
	logField(evt,"motion", evt.value.toString() == "active" ? 1 : -1)
}

def handleSwitchEvent(evt) {
	logField(evt,"switch", evt.value.toString() == "on" ? 1 : -1)
}

def handleLightEvent(evt) {
	logField(evt,"illuminance", evt.value.toString())
}

def handleHumidityEvent(evt) {
	logField(evt,"humidity", evt.value.toString())
}

def handleBatteryEvent(evt) {
	logField(evt,"battery", evt.value.toString())
}

def handleEnergyEvent(evt) {
	logField(evt,"energy", evt.value.toString())
}

def handlePowerEvent(evt) {
	logField(evt,"power", evt.value.toString())
}

def lanResponseHandler(evt){
	log.debug("Lan Response: ${evt.description}")
}

private logField(evt, metric, value) {
	def deviceName = evt.displayName.trim().toLowerCase().replaceAll(' ','_').replaceAll('/','_')
	def nowTime = new Date().format( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",location.timeZone)
	def body = """json={"timestamp":"$nowTime","deviceName":"$deviceName","$metric":$value}"""
	postapi(body)
}

private postapi(command) {
	def length = command.getBytes().size().toString()
	sendHubCommand(new physicalgraph.device.HubAction("""POST /house.metric HTTP/1.1\r\nHOST: xxx.xxx.xxx.xxx:pppp\r\nContent-Type: application/x-www-form-urlencoded\r\nContent-Length: ${length}\r\nAccept:*/*\r\n\r\n${command}""", physicalgraph.device.Protocol.LAN, "XXXXXXXX:PPP"))
}
