QueryString=
    {
        data   :{},
        Init:function()
        {
            var   aPairs,   aTmp;
            var   queryString   =   new String(window.location.search);
            queryString   =   queryString.substr(1,   queryString.length);   //remove   "?"
            aPairs   =   queryString.split("&");
            for   (var   i=0   ;   i<aPairs.length;   i++)
            {
                aTmp   =   aPairs[i].split("=");
                this.data[aTmp[0]]   =   aTmp[1];
            }
           // console.log(this.data);
        },
        GetValue:function(key)
        {
            return   this.data[key];
        },
        SetValue:function(   key,   value   )
        {
            if   (value   ==   null)
                delete   this.data[key];
            else
                this.data[key]   =   value;
        },
        ToString:function()
        {
            var   queryString   =   new   String("");

            for   (var   key   in   this.data)
            {
                if   (queryString   !=   "")
                    queryString   +=   "&";
                if   (this.data[key])
                    queryString   +=   key   +   "="   +   this.data[key];
            }
            if   (queryString.length   >   0)
                return   "?"   +   queryString;
            else
                return   queryString;
        },
        Clear:function()
        {
            delete   this.data;
            this.data   =   [];
        }
    };