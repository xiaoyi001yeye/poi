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

import org.apache.poi.ddf.*;
import org.apache.poi.hslf.record.OEPlaceholderAtom;

import java.util.List;
import java.io.ByteArrayOutputStream;

/**
 * Represents a Placeholder in PowerPoint.
 *
 * @author Yegor Kozlov
 */
public class Placeholder extends TextBox {

    protected Placeholder(EscherContainerRecord escherRecord, Shape parent){
        super(escherRecord, parent);
    }

    public Placeholder(Shape parent){
        super(parent);
    }

    public Placeholder(){
        super();
    }

    /**
     * Create a new Placeholder and initialize internal structures
     *
     * @return the created <code>EscherContainerRecord</code> which holds shape data
     */
    protected EscherContainerRecord createSpContainer(boolean isChild){
        EscherContainerRecord spcont = super.createSpContainer(isChild);

        EscherSpRecord spRecord = spcont.getChildById(EscherSpRecord.RECORD_ID);
        spRecord.setFlags(EscherSpRecord.FLAG_HAVEANCHOR | EscherSpRecord.FLAG_HAVEMASTER);

        EscherClientDataRecord cldata = new EscherClientDataRecord();
        cldata.setOptions((short)15);

        EscherOptRecord opt = (EscherOptRecord)getEscherChild(spcont, EscherOptRecord.RECORD_ID);

        //Placeholders can't be grouped
        setEscherProperty(opt, EscherProperties.PROTECTION__LOCKAGAINSTGROUPING, 262144);

        //OEPlaceholderAtom tells powerpoint that this shape is a placeholder
        //
        OEPlaceholderAtom oep = new OEPlaceholderAtom();
        /**
         * Extarct from MSDN:
         *
         * There is a special case when the placeholder does not have a position in the layout.
         * This occurs when the user has moved the placeholder from its original position.
         * In this case the placeholder ID is -1.
         */
        oep.setPlacementId(-1);

        oep.setPlaceholderId(OEPlaceholderAtom.Body);

        //convert hslf into ddf record
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            oep.writeOut(out);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
        cldata.setRemainingData(out.toByteArray());

        //append placeholder container before EscherTextboxRecord
        List lst = spcont.getChildRecords();
        for (int i = 0; i < lst.size(); i++) {
              EscherRecord rec = (EscherRecord)lst.get(i);
              if(rec.getRecordId() == EscherTextboxRecord.RECORD_ID){
                  lst.add(i++, cldata);
              }
        }

        return spcont;
    }
}
