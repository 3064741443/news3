--管理员信息表
create table admin(
	aid int primary key,  --管理员编号
	aname varchar2(100) unique not null,  --管理员姓名
	pwd varchar2(36)      --管理员密码
);
create sequence seq_admin_aid start with 1001  increment by 1;
insert into admin values(seq_admin_aid.nextval,'navy','aaaaaa');

select * from admin;

--会员信息表
create table users(
	usid int primary key, --会员编号
	uname varchar2(100) unique not null,   --会员名称
	pwd varchar2(36),   --登录密码
	email varchar2(100) unique not null,  --注册邮箱
	status int  --状态
);
create sequence seq_users_usid start with 1001 increment by 1;

--新闻类型表
create table newsType(
	tid int primary key,  --类型编号
	tname varchar2(100),  --类型名称
	status int  --状态
);
create sequence seq_newsType_tid start with 1001 increment by 1;


--新闻内容表
create table news(
	nid int primary key,  --新闻编号
	title varchar2(400) not null, --新闻标题
	ndate date,  --最后修改日期
	content varchar2(5000),  --新闻内容  <img src="">
	auth varchar2(100), --作者
	pic varchar2(4000), --图片地址
	tid int
		constraint FK_news_newsType_tid references newsType(tid),
	views int,  --浏览次数
	weight int  --权重
);
create sequence seq_news_nid start with 1001 increment by 1;


select n.*,tname from news n,newsType t where n.tid=t.tid;


select * from admin;


select * from (select a.*,rownum rn from (select n.*,tname from news n,newstype t where n.tid=t.tid order by weight desc,ndate desc) a where rownum<=?)b where rn>?


select * from (select a.*,rownum rn from (select nid,title from news n,newstype t where n.tid=t.tid and tname=? order by weight desc,ndate desc) a where rownum<=?)b where rn>?


select * from (select a.*,rownum rn from (select nid,title from news n,newstype t where n.tid=t.tid and tname=? order by weight desc,ndate desc) a where rownum<=?)b where rn>? unionselect * from (select a.*,rownum rn from (select nid,title from news n,newstype t where n.tid=t.tid and tname=? order by weight desc,ndate desc) a where rownum<=?)b where rn>? union select * from (select a.*,rownum rn from (select nid,title from news n,newstype t where n.tid=t.tid and tname=? order by weight desc,ndate desc) a where rownum<=?)b where rn>?


select * from (select a.*,rownum rn from (select nid,title,auth,ndate,views from news n,newstype t where n.tid=t.tid and ndate=? and n.tid=? order by weight desc,ndate desc) a where rownum<=?)b where rn>?

select * from (select a.*,rownum rn from (select nid,title,auth,ndate,views from news n,newstype t where n.tid=t.tid and title like ? order by weight desc,ndate desc) a where rownum<=?)b where rn>?
