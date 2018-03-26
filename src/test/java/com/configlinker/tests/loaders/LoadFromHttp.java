package com.configlinker.tests.loaders;

import com.configlinker.annotations.BoundObject;

@BoundObject( sourcePath = "http://localhost:5050/http_config.properties")
interface LoadFromHttp
{

}
