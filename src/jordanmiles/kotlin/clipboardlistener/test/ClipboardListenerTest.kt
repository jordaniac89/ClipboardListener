package jordanmiles.kotlin.clipboardlistener.test

import jordanmiles.kotlin.clipboardlistener.*

import java.awt.datatransfer.DataFlavor

fun main( args: Array<String> ) {
	
	val listener: ClipboardListener = ClipboardListener(
		changeHandler = {
				println( "Data inside changeHandler: ${ it.getData( DataFlavor.stringFlavor ) }" )
		}
	)
	
	listener.startPolling()
	
}

