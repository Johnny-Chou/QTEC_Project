package com.im.qtec.entity;

/**
 * Created by zhouyanglei on 2017/12/20.
 */

public class KeyResultEntity {


    /**
     * flag : true
     * errCode :
     * resData : {"value":"5gw6aiAsWS/lGzszC3/prfO56BJp0TOJzYq1oILYFh7q3o/IEXDH5OYI7v6RvuSPNFpPq7MnaPQn84Y4SMCfYRLRGR3VNe6rMKBfoKOwawG48jJz8eRHfEGXtqyk8HqPMgUgQO3snqz0bBPS1vFqqa4EIx6lyAxE/NNNFMq/n1+Dlru55kbfDgV/oreoAKZOkdJ860d+CJEtDnbJzBdYo8G9L0Mr5dklCoHFXhVQ2UX6yZp8aTGvRWZhbq6VrknMBIcLQuSfNqcoy0UogvRI1d/mTE8rrJkoZQuKvsbTJ4MTTx1DiZL3d+Px4v8qIy8uPacQSVBTj1D5Z1VUnC66HDpeopXL6jj6nqpG8GQExNUKb9Ovb2sNU+tphtclKZFmqjDw++Nxn2sFogB5twQm6ZFX9fuuX5pwMsOtBiqpkP6RhBKGMO94S+2HTvR+YIaGLtZg7C/WR9llehvjKIoe0O3bjsf3X/I47YyNoujG/sNubyvXC2QMy2hT+lKe2Wk/fRzWhoAK22bh6EilekwOOgXfdzaM4C6nVblmqesdqTTO5i1f7BOLCSr9/dp4wWtU3Znqv9GEJgm/TiKxIgDN49Ec498xgm9kZ65VZ60FIxUzeWISTiyKvz1RkODvffYbmdAbjs8zRxIPlE2ULhMKzgmShG1W/9JN4tD0yoIxroAN/T0B7Z9kVxzh3zfm0nMPi8gb4Sxzg4hQsmM1lhCMSQO5AqRhVqGD4TOM+iVjoYnq7wbu87B8P/1iAzuiWqOWMZemJk8ZbdJzI8NkBdHQChOwmRzq0Ymo6z+nsHr68QdRuJJb5sbB9gZBFgy0BgOC3RnrPt5D/Cj4gRwz9ZtNBB/1W/FTOYEyw7czK69xawt4FgUNGOLfrHILQQXTyd7zhH7ZsSLu7iMXDclRkRAWR49BgF6G1MGmY6jEzGqY+wg/14p7382g7St+rvshNE3NRFUmP920TFW6fedbppUJwqIv38vgf1v9HwdndukSSTmZE9K2qlNFbjyPqrER0x6oBO+QpQ5u+2TbvUf7qSh9g/PFTxbaevAlx5Mnc2/dOhgq2tCjLByx7lvliWQv01XmwWNUF0+fvTzLny3YlwgY2EYPuZw9iSTNb9zX0HvssqPWnyllF+K3z0CGctUmfbySdSj2/rpRmrA6TS75w1jRIbqKos9kmNHwKG0RGcOk48mrjMu+l/Nf40MVAqcR00H5YZ+v0WI/TtQ74wkRjd8xhDkWXNw4mT+xUu2mAyr8UOIgqsim6wAQ6k7c+A8AFSkzgPuwCAIm4WiuxkssN5TciK9PSBoiLg9MRWEUXs+9jkhHGjRpZN0EHQ==","bookid":"e42b6144-ce29-46f0-ac9f-2ffc50bdb9a0"}
     */

    private boolean flag;
    private String errCode;
    private ResDataBean resData;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public ResDataBean getResData() {
        return resData;
    }

    public void setResData(ResDataBean resData) {
        this.resData = resData;
    }

    public static class ResDataBean {
        /**
         * value : 5gw6aiAsWS/lGzszC3/prfO56BJp0TOJzYq1oILYFh7q3o/IEXDH5OYI7v6RvuSPNFpPq7MnaPQn84Y4SMCfYRLRGR3VNe6rMKBfoKOwawG48jJz8eRHfEGXtqyk8HqPMgUgQO3snqz0bBPS1vFqqa4EIx6lyAxE/NNNFMq/n1+Dlru55kbfDgV/oreoAKZOkdJ860d+CJEtDnbJzBdYo8G9L0Mr5dklCoHFXhVQ2UX6yZp8aTGvRWZhbq6VrknMBIcLQuSfNqcoy0UogvRI1d/mTE8rrJkoZQuKvsbTJ4MTTx1DiZL3d+Px4v8qIy8uPacQSVBTj1D5Z1VUnC66HDpeopXL6jj6nqpG8GQExNUKb9Ovb2sNU+tphtclKZFmqjDw++Nxn2sFogB5twQm6ZFX9fuuX5pwMsOtBiqpkP6RhBKGMO94S+2HTvR+YIaGLtZg7C/WR9llehvjKIoe0O3bjsf3X/I47YyNoujG/sNubyvXC2QMy2hT+lKe2Wk/fRzWhoAK22bh6EilekwOOgXfdzaM4C6nVblmqesdqTTO5i1f7BOLCSr9/dp4wWtU3Znqv9GEJgm/TiKxIgDN49Ec498xgm9kZ65VZ60FIxUzeWISTiyKvz1RkODvffYbmdAbjs8zRxIPlE2ULhMKzgmShG1W/9JN4tD0yoIxroAN/T0B7Z9kVxzh3zfm0nMPi8gb4Sxzg4hQsmM1lhCMSQO5AqRhVqGD4TOM+iVjoYnq7wbu87B8P/1iAzuiWqOWMZemJk8ZbdJzI8NkBdHQChOwmRzq0Ymo6z+nsHr68QdRuJJb5sbB9gZBFgy0BgOC3RnrPt5D/Cj4gRwz9ZtNBB/1W/FTOYEyw7czK69xawt4FgUNGOLfrHILQQXTyd7zhH7ZsSLu7iMXDclRkRAWR49BgF6G1MGmY6jEzGqY+wg/14p7382g7St+rvshNE3NRFUmP920TFW6fedbppUJwqIv38vgf1v9HwdndukSSTmZE9K2qlNFbjyPqrER0x6oBO+QpQ5u+2TbvUf7qSh9g/PFTxbaevAlx5Mnc2/dOhgq2tCjLByx7lvliWQv01XmwWNUF0+fvTzLny3YlwgY2EYPuZw9iSTNb9zX0HvssqPWnyllF+K3z0CGctUmfbySdSj2/rpRmrA6TS75w1jRIbqKos9kmNHwKG0RGcOk48mrjMu+l/Nf40MVAqcR00H5YZ+v0WI/TtQ74wkRjd8xhDkWXNw4mT+xUu2mAyr8UOIgqsim6wAQ6k7c+A8AFSkzgPuwCAIm4WiuxkssN5TciK9PSBoiLg9MRWEUXs+9jkhHGjRpZN0EHQ==
         * bookid : e42b6144-ce29-46f0-ac9f-2ffc50bdb9a0
         */

        private String value;
        private String bookid;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getBookid() {
            return bookid;
        }

        public void setBookid(String bookid) {
            this.bookid = bookid;
        }
    }
}
