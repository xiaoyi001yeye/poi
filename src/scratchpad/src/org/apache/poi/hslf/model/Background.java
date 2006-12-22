
/* ====================================================================
   Copyright 2002-2004   Apache Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hslf.model;

import org.apache.poi.ddf.EscherContainerRecord;

/**
 * Background shape
 * 
 * @author Yegor Kozlov
 */
public class Background extends Shape {

    protected Background(EscherContainerRecord escherRecord, Shape parent){
        super(escherRecord, parent);
    }

    protected EscherContainerRecord createSpContainer(boolean isChild){
        return null;
    }

}
