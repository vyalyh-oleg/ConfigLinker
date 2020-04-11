/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <crispcode.net@gmail.com>

      Licensed under the Apache License, Version 2.0 (the "License"); you may not
      use this file except in compliance with the License. You may obtain a copy
      of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
      WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
      License for the specific language governing permissions and limitations
      under the License.
 */


package net.crispcode.configlinker.exceptions;


public class FactoryConfigBuilderClosedException extends ConfigLinkerRuntimeException
{
	private static final String message = "You can not change parameter or add property to closed FactorySettingsBuilder. It is closed, because it was already used in ConfigSetFactory.";
	
	private static final FactoryConfigBuilderClosedException instance = new FactoryConfigBuilderClosedException();
	
	private FactoryConfigBuilderClosedException()
	{
		super(FactoryConfigBuilderClosedException.message);
	}
	
	public static FactoryConfigBuilderClosedException getInstance()
	{
		return instance;
	}
}
