```
--------------------------------------------------------  
-- Triggers  
-- 参考：https://blog.csdn.net/kongxx/article/details/5884359
--------------------------------------------------------

CREATE OR REPLACE TRIGGER "ACL_CLASS_ID"  
BEFORE INSERT ON ACL_CLASS  
FOR EACH ROW  
  BEGIN  
    SELECT ACL_CLASS_SEQ.NEXTVAL INTO :new.id FROM dual;  
  END;  
/  
   
CREATE OR REPLACE TRIGGER "ACL_ENTRY_ID"  
BEFORE INSERT ON ACL_ENTRY  
FOR EACH ROW  
  BEGIN  
    SELECT ACL_ENTRY_SEQ.NEXTVAL INTO :new.id FROM dual;  
  END;  
/  
   
CREATE OR REPLACE TRIGGER "ACL_OBJECT_IDENTITY_ID"  
BEFORE INSERT ON ACL_OBJECT_IDENTITY  
FOR EACH ROW  
  BEGIN  
    SELECT ACL_OBJECT_IDENTITY_SEQ.NEXTVAL INTO :new.id FROM dual;  
  END;  
/  
   
CREATE OR REPLACE TRIGGER "ACL_SID_ID"  
BEFORE INSERT ON ACL_SID  
FOR EACH ROW  
  BEGIN  
    SELECT ACL_SID_SEQ.NEXTVAL INTO :new.id FROM dual;  
  END;  
/  
```