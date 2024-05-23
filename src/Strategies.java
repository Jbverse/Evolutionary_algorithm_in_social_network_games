import java.util.ArrayList;

public class Strategies {
    private Functions functions = new Functions();

//    public int percentageStrategy(int percent) {
//        int newChoose = (int) (Math.random() * 100 + 1);
//        return newChoose > percent ? 0 : 1;
//    }


    //מידה כנגד מידה
    public int titForTat(ArrayList<Integer> opponents, int sex) {
        int choose0 = 0;
        int choose1 = 0;

        for (int opponent : opponents) {

            int opponentChoose = AgentsList.getAgent(opponent).getState();
            int opponentSex = AgentsList.getAgent(opponent).getSex();

            //אם דילמת האסיר - אם בחר לדפוק, תדפוק תור הבא. ולהיפך
            if (sex == -1) {
                if (opponentChoose == 0) choose0++;
                else choose1++;
            }


            //אם מלחמת המינים
            else {
                //אם הבחירה שלו זהה למין שלי (הלך איתי) אני אלך איתו תור הבא
                if (sex - opponentChoose == 0) {
                    //אם הוא גבר: מוסיף 1 לבחירה של הגבר ו-0 לבחירה של האישה. ולהיפך
                    choose0 += (1 - opponentSex);
                    choose1 += opponentSex;
                }
                //אם הוא הלך נגדי, אני לא מתחשב בו תור הבא
                else {
                    //אם אני גבר: מוסיף 1 לבחירה של הגבר ו-0 לבחירה של האישה. ולהיפך
                    choose0 += opponentChoose;
                    choose1 += sex;
                }
            }

        }
        if (choose0 > choose1) return 0;
        return 1;
    }

    public int thresholdStrategy(int threshold, ArrayList<Integer> opponents, int sex) {
        int t = 0;
        for (int opponent : opponents) {
            int opponentChoose = AgentsList.getAgent(opponent).getState();
            int opponentSex = AgentsList.getAgent(opponent).getSex();

            //אם דילמת האסיר - תחשב כמה שיתפו פעולה תור קודם, עד שיהיה רווחי לדפוק אותם
            if (sex == -1) {
                if (opponentChoose == 1) t++;
                if (t >= threshold) return 0;
            }

            //אם מלחמת המינים אם מעל t מסויים הלכו נגדי, שווה לשתף איתם פעולה
            else {
                //יוסיף לחישוב רק אם המינים שלהם שונים! אם המינים זהים, שיתוף פעולה הוא להשאר בבחירה הטריוויאלית
                if (opponentChoose != sex) t += Math.abs(sex - opponentSex);
                //אם עבר את הערך הסף, שתף פעולה עם המין השני
                if (t >= threshold) return 1 - sex;
            }

        }

        //אם לא עבר את ערך הסף: באסיר(-1) שתף פעולה (תחזיר 1) ובמינים תחזיר את הבחירה הטובה עבור המין שלך (0-אישה, 1-איש)
        return Math.abs(sex);
    }

    public int bestResStrategy(ArrayList<Integer> opponents, int sex) {
        int choose0 = 0;
        int choose1 = 0;
        int finalChoose;

        for (int opponent : opponents) {
            int opponentState = AgentsList.getAgent(opponent).getState();
            choose0 += functions.gainMatrix(sex)[0][opponentState];
            choose1 += functions.gainMatrix(sex)[1][opponentState];
        }

        if (choose0 >= choose1) finalChoose = 0;
        else finalChoose = 1;

        return finalChoose;
    }

    //אדם ישר
    public int honestStrategy(ArrayList<Integer> opponents, int sex) {
        //אם לא אסיר
        if (sex != -1) {
            //אם 30 אחוז מהשכנים שלי בעלי מין זהה לשלי, עדיף ללכת איתם מבחינה רווחית
            int threshold = (int) Math.round(opponents.size() * 0.3);
            int t = 0;

            for (int opponent : opponents) {
                int opponentSex = AgentsList.getAgent(opponent).getSex();
                if (opponentSex == sex) t++;

                if (t >= threshold) return sex;
                //אחרת, אם אני גבר תחזיר אישה (0) ואם אני אישה תחזיר גבר (1)
                return 1 - sex;
            }

        }
        //אם אסיר
        return 1;
    }


    public int evilStrategy(int sex) {
        //אם אסיר תחזיר 0 (תדפוק) אם מלחמת המינים תבחר את מה שטוב לך
        return (sex != -1) ? sex : 0;
    }


}
