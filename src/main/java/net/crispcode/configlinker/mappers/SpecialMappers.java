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


package net.crispcode.configlinker.mappers;


import java.nio.file.Path;
import java.nio.file.Paths;


final class SpecialMappers
{
	static char characterMapper(String raw)
	{
		if (raw.length() > 1)
			throw new IllegalArgumentException("Given string '" + raw + "' instead of a single char.");
		return raw.charAt(0);
	}
	
	static Path pathMapper(String raw)
	{
		return Paths.get(raw);
	}
}
