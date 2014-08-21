# Gitlab Integration Plugin

Lets you interact with gitlab from within your IDE.

Features:
<ul>
    <li>List projects (by namespaces) and their issues</li>
    <li>Filter issues by author, assignee or custom text</li>
    <li>Re-open / close issues (right click)</li>
    <li>Edit issues (double click)</li>
</ul>

Please, leave a comment or drop me an email with any issues/reports.

<b>Note: this plugin requires intellij to be started with Java7 due to third party libs.</b>

## How To

After plugin install, go to IDE preferences, and look for Gitlab Integration.<br>
You'll need the host and your API key from your gitlab (which you can find under Profile Settings -> Account)

## Change notes

( in development ) <b>v1.0.6</b><br>
<ul>
    <li>UI minor improves</li>
</ul>
( 2014-07-08 ) <b>v1.0.5</b><br>
<ul>
    <li>Fixed possible ssl error (ignoring certificate errors by default)</li>
</ul>
( 2014-07-07 ) <b>v1.0.4</b><br>
<ul>
    <li>bug fixes</li>
</ul>
( 2014-06-23 ) <b>v1.0.3</b><br>
<ul>
    <li>Issues list filter</li>
    <li>minor improvements</li>
</ul>
( 2014-04-29 ) <b>v1.0.2</b><br>
<ul>
    <li>Set plugin dependency to build 133 (IntelliJ IDEA 13, PyCharm 3.1, WebStorm 7, PhpStorm 7) - intellij 13 was the first to support java7</li>
</ul>
( 2014-04-29 ) <b>v1.0.1</b>
<ul>
    <li>Improves in the issue editing view</li>
</ul>
( 2014-04-25 ) <b>v1.0</b><br>
<ul>
    <li>Plugin creation</li>
</ul>

## Author

Author:: Diogo Neves ( <a href="mailto:diogo.sousa.neves@gmail.com">diogo.sousa.neves@gmail.com</a> )

## Licence

The MIT License (MIT)

Copyright (c) 2014 Luis Nabais

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

