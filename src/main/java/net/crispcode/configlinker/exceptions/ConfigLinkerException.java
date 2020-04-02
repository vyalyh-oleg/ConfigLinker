/*
      Copyright 2019, Vyalyh Oleg Olegovich,
      <vyalyh.oleg@gmail.com>, <crispcode.net@gmail.com>, <oleg@crispcode.net>

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

import net.crispcode.configlinker.Loggers;


public abstract class ConfigLinkerException extends Exception {
	private ConfigLinkerException() {
		super();
	}

	public ConfigLinkerException(String message) {
		super(message);
	}

	public ConfigLinkerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigLinkerException(Throwable cause) {
		super(cause);
	}

	public ConfigLinkerException logAndReturn() {
		Loggers.getMainLogger().error(this.getMessage(), this);
		return this;
	}
}
