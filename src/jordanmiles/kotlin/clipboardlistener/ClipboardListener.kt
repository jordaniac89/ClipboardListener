package jordanmiles.kotlin.clipboardlistener

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.fixedRateTimer


data class ClipboardListener @JvmOverloads constructor(
		val clipboard: Clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(),
		val changeHandler: (Clipboard) -> Unit
		) {
	
	private var _timer: Timer? = null

	private var _previousValue: String? = null
	public val previousValue: String
		get() {
			
			if( _previousValue == null ) {
				_previousValue = previousValue
			}
			
			return _previousValue ?: throw Exception( "Set to null by another thread" )
		}
	
	private var _task: (TimerTask.() -> Unit)? = null	
	public val task: TimerTask.() -> Unit	
		get() {
			
			if( _task == null ) {
				_task = {
					if( _previousValue != clipboard.getData( DataFlavor.stringFlavor ).toString() ) {
												
						changeHandler( clipboard )
						
						_previousValue = clipboard.getData( DataFlavor.stringFlavor ).toString()
						
					}
				}
			}
			
			return _task ?: throw Exception( "Set to null by another thread" )
		}
	
	public fun startPolling() {
		
		_previousValue = clipboard.getData( DataFlavor.stringFlavor ).toString()
		
		println( "Clipboard event listener initialized -> ${ clipboard.name }" )
		
		println( "Polling started..." )
		
		_timer = fixedRateTimer(
				name = "Clipboard poller",
				daemon = false,
				initialDelay = 0,
				period = 250,
				action = task )
	}
	
	public fun stopPolling() {
		_timer?.cancel()
	}
}

