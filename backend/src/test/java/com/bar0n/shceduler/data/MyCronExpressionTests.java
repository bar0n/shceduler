package com.bar0n.shceduler.data;

import com.bar0n.shceduler.services.MyCronExpression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronExpression;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by dbaron
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyCronExpressionTests {

    @Test
    //@Rollback(true)
    public void manyToOneTest() throws ParseException {
        String s = "0 0 20 ? * FRI%2";
        String s2 = "0 0 20 ? * FRI";
        String expression = "0 */10 * ? * *";
        CronExpression cronExpression = new CronExpression(s);
        MyCronExpression myCronExpression = new MyCronExpression(s);
        MyCronExpression myCronExpression2 = new MyCronExpression(s2);
        ZonedDateTime now = ZonedDateTime.now();
        Stream.iterate(new Date(),d->myCronExpression2.getNextValidTimeAfter(d,now))
                .limit(5)
                .forEach(x->{
                    System.out.println(x);

                } );
        System.out.println();
        Stream.iterate(new Date(),d->cronExpression.getNextValidTimeAfter(d))
                .limit(5)
                .forEach(x->{
                    System.out.println(x);

                });

        System.out.println();
        Stream.iterate(new Date(),d->myCronExpression.getNextValidTimeAfter(d,now))
                .limit(5)
                .forEach(x->{
                    System.out.println(x);

                } );
        //   MyCronExpression myCronExpression2 = new MyCronExpression(s);
       /* Date nextValidTimeAfter2 = myCronExpression2.getNextValidTimeAfter(new Date(), ZonedDateTime.now());
        Date nextValidTimeAfter = myCronExpression.getNextValidTimeAfter(new Date(), ZonedDateTime.now());
        System.out.println(nextValidTimeAfter);
        System.out.println(nextValidTimeAfter2);*/
    }



}
