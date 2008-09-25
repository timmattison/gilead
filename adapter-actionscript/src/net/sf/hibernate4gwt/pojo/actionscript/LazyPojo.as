package net.sf.hibernate4gwt.pojo.actionscript
{
	import mx.collections.ArrayCollection;
	
    [RemoteClass(alias="net.sf.hibernate4gwt.pojo.java5.LazyPojo")]
	public class LazyPojo
	{
		/**
		 * The internal proxy informations
		 * (a map of <String, Map<String, bytes>>)
		 */
		private var _proxyInformations:Object;
		
		/**
		 * Constructor of Lazy Pojo abstract class.
		 **/
		public function LazyPojo()
		{
			_proxyInformations = new Object();
		}
		
		/**
		 * Getter for proxy informations
		 */
		public function get proxyInformations():Object
		{
			return _proxyInformations;
		}
		
		/**
		 * Setter for proxy informations
		 */
		public function set proxyInformations(value:Object):void
		{
			_proxyInformations = value;
		}
		
		/**
		 * Remove proxy informations.
		 * Use with care, especially on collections !!!
		 */
		public function removeProxyInformation(propertyName:String):void
		{
			if (_proxyInformations != null)
			{
				_proxyInformations[propertyName] = null;
			}
		}
		
		/**
		 * toString overrides
		 */
		public function toString():String
		{
			return "[proxyInformations : "+_proxyInformations.toString()+"]";
		}
	}
}
