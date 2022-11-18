package com.example.passwordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Arrays with all available characters, divided by type.
    String [] lowercase_consonants = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"};
    String [] lowercase_vowels = {"a", "e", "i", "o", "u"};
    String [] uppercase_consonants = {"B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "X", "Y", "Z"};
    String [] uppercase_vowels = {"A", "E", "I", "O", "U"};
    String [] numbers = {"1", "2", "3", "4", "5", "5", "6", "7", "8", "9", "0"};
    String [] special_characters = {"!", "£", "$", "%", "&", "?", "@", "#"};

    //Elements declaration
    CheckBox lowc; //Checkboxes to select character types
    CheckBox uppc;
    CheckBox num;
    CheckBox schar;

    Switch readable_switch; //Switch to specify if the password needs to be readable

    Random r; //Random number generator

    ArrayList<String> c; //ArrayList to contain all the characters that can be in the password, according to the user choices

    String password; //Generated password

    TextView pw_textview; //Textview to show the generated password

    TextView length; //Textview to show the length of the password
    int len; //Length of the password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Elements initialization: find elements by id specified in activity_main.xml
        lowc=findViewById(R.id.lowercase);
        uppc=findViewById(R.id.uppercase);
        num=findViewById(R.id.numbers);
        schar=findViewById(R.id.special_characters);

        readable_switch=findViewById(R.id.readable);

        pw_textview=findViewById(R.id.password);

        length=findViewById(R.id.length);
        len=Integer.parseInt(length.getText().toString());

        r = new Random();
    }

    //Increment length of the password (max length 16)
    public void add (View view)
    {
        if(len<16) {
            len++;
            length.setText(String.valueOf(len));
        }
    }

    //Decrement length of the password (min length 4)
    public void sub (View view)
    {
        if(len>4) {
            len--;
            length.setText(String.valueOf(len));
        }
    }

    //It is not possible to have a readable password without letters:
    //if the user disables both uppercase and lowercase letters, the switch to get a readable password is disabled too
    public void disable_readable(View view)
    {
        if(!lowc.isChecked() && !uppc.isChecked())
        {
            readable_switch.setChecked(false);
        }
    }

    //It is not possible to have a readable password without letters:
    //if the user enables the switch to get a readable password and uppercase and lowercase letters are disabled,
    //both letters checkboxes are enabled
    public void enable_letters(View view)
    {
        if(readable_switch.isChecked() && !lowc.isChecked() && !uppc.isChecked())
        {
            lowc.setChecked(true);
            uppc.setChecked(true);
        }
    }

    //This function is called when the users clicks on "Generate password" button
    public void generate_password (View view)
    {
        if(readable_switch.isChecked())
        {
            generate_readable();
        }
        else
        {
            generate_random();
        }
    }

    public void generate_random()
    {
        //Create new array ArrayList to contain all the characters of the chosen types
        //ArrayList is a variable-length structure
        c = new ArrayList<>();

        //Add lowercase letters
        if(lowc.isChecked())
        {
            for(int i=0; i<lowercase_consonants.length; i++)
            {
                c.add(lowercase_consonants[i]);
            }
            
            for(int i=0; i<lowercase_vowels.length; i++)
            {
                c.add(lowercase_vowels[i]);
            }
        }

        //Add uppercase letters
        if(uppc.isChecked())
        {
            for(int i=0; i<uppercase_consonants.length; i++)
            {
                c.add(uppercase_consonants[i]);
            }

            for(int i=0; i<uppercase_vowels.length; i++)
            {
                c.add(uppercase_vowels[i]);
            }
        }

        //Add numbers
        if(num.isChecked())
        {
            for(int i=0; i<numbers.length; i++)
            {
                c.add(numbers[i]);
            }
        }

        //Add special characters
        if(schar.isChecked())
        {
            for(int i=0; i<special_characters.length; i++)
            {
                c.add(special_characters[i]);
            }
        }

        //Build a new string of the specified length
        do {
            password = "";

            //Take [len] random characters from ArrayList c and append them to the string
            if(!c.isEmpty()) {
                for (int i = 0; i < len; i++) {
                    password += c.toArray()[r.nextInt(c.size())];
                }
            }
        }while(!checkpassword()); //If the string doesn't match the requirements, go back and build a new one

        pw_textview.setText(password); //Show the password in the textview
    }

    //Return true if the random generated password matches the requirements; otherwise return false
    private boolean checkpassword()
    {
        //4 boolean variables to check the presence of each character type.
        boolean checklowc=false;
        boolean checkuppc=false;
        boolean checknum=false;
        boolean checkschar=false;

        int i; //Counter

        //For each type of character, the corresponding boolean variable is set true in 2 cases:
        //- if this type is not required;
        //- if this type is required and at least one character of this type is found in the string.
        //Otherwise the boolean variable remains false.

        //Lowercase letters
        if(lowc.isChecked())
        {
            i=0;
            while(i<lowercase_consonants.length && !checklowc)
            {
                if (password.contains(lowercase_consonants[i])) {
                    checklowc=true;
                }
                i++;
            }

            i=0;
            while(i<lowercase_vowels.length && !checklowc)
            {
                if (password.contains(lowercase_vowels[i])) {
                    checklowc=true;
                }
                i++;
            }
        }
        else
        {
            checklowc=true;
        }

        //Uppercase letters
        if(uppc.isChecked())
        {
            i=0;
            while(i<uppercase_consonants.length && !checkuppc)
            {
                if (password.contains(uppercase_consonants[i])) {
                    checkuppc=true;
                }
                i++;
            }

            i=0;
            while(i<uppercase_vowels.length && !checkuppc)
            {
                if (password.contains(uppercase_vowels[i])) {
                    checkuppc=true;
                }
                i++;
            }
        }
        else
        {
            checkuppc=true;
        }

        //Numbers
        if(num.isChecked())
        {
            i=0;
            while(i<numbers.length && !checknum)
            {
                if (password.contains(numbers[i])) {
                    checknum=true;
                }
                i++;
            }
        }
        else
        {
            checknum=true;
        }

        //Special characters
        if(schar.isChecked())
        {
            i=0;
            while(i<special_characters.length && !checkschar)
            {
                if (password.contains(special_characters[i])) {
                    checkschar=true;
                }
                i++;
            }
        }
        else
        {
            checkschar=true;
        }

        //Return true if all of the 4 boolean variables are true
        return checklowc && checkuppc && checknum && checkschar;
    }

    public void generate_readable()
    {
        //Conditions of a readable password
        //Characters types, if selected, appear in the following order: letters, numbers and special characters.
        //If both uppercase and lowercase letters are selected, only the first letter is uppercase.
        //Vowels and consonants are alternating

        //Four integers to indicate how many characters there are for each type
        int g1=0; //Uppercase
        int g2=0; //Lowercase letters
        int g3=0; //Numbers
        int g4=0; //Special characters

        //N° of special characters (if selected): random number from 1 to a max of 1/4 of the total length
        if(schar.isChecked())
        {
            g4=1+r.nextInt(len/4);
        }

        //N° of numbers (if selected): random number from 1 to a max of 1/3 of the remaining length (without considering the special characters)
        if(num.isChecked())
        {
            g3=1+r.nextInt((len-g4)/3);
        }

        //Letters
        if(lowc.isChecked())
        {
            if(uppc.isChecked()) //The first letter is uppercase, the other letters are lowercase
            {
                g1=1;
                g2=len-g1-g3-g4;
            }
            else //All the letters are lowercase
            {
                g2=len-g3-g4;
            }
        }
        else //All the letters are uppercase
        {
            g1=len-g3-g4;
        }

        //Build a new string, each type of character of the specified length
        password = "";

        //Add letters (first uppercase, then lowercase, alternating consonants and vowels
        for (int i = 0; i < g1; i++) {
            if(i%2==0)
            {
                password += uppercase_consonants[r.nextInt(uppercase_consonants.length)];
            }
            else
            {
                password += uppercase_vowels[r.nextInt(uppercase_vowels.length)];
            }

        }
        for (int i = 0; i < g2; i++) {
            if((g1+i)%2==0)
            {
                password += lowercase_consonants[r.nextInt(lowercase_consonants.length)];
            }
            else
            {
                password += lowercase_vowels[r.nextInt(lowercase_vowels.length)];
            }
        }

        //Add numbers
        for (int i = 0; i < g3; i++) {
            password += numbers[r.nextInt(numbers.length)];
        }

        //Add special characters
        for (int i = 0; i < g4; i++) {
            password += special_characters[r.nextInt(special_characters.length)];
        }

        pw_textview.setText(password); //Show the password in the textview
    }

    //Function to copy the password in the clipboard and inform the user with a toast.
    public void copy(View view) {
        try {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", password);
            clipboard.setPrimaryClip(clip);
            String message = getResources().getString(R.string.copy_message);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            String message = getResources().getString(R.string.error);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}