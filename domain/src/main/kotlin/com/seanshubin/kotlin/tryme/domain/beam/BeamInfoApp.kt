package com.seanshubin.kotlin.tryme.domain.beam

import com.seanshubin.kotlin.tryme.domain.beam.Conversions.toBeamFileSummary
import com.seanshubin.kotlin.tryme.domain.cursor.Cursor
import com.seanshubin.kotlin.tryme.domain.cursor.IteratorCursor
import com.seanshubin.kotlin.tryme.domain.io.ioutil.toLineIterator
import com.seanshubin.kotlin.tryme.domain.string.ByteArrayFormatHex
import com.seanshubin.kotlin.tryme.domain.string.ByteArrayFormatHex.toLines
import java.nio.file.Files
import java.nio.file.Paths

// https://beam-wisdoms.clau.se/en/latest/indepth-beam-file.html
object BeamInfoApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val file = Paths.get(args[0])
        val inputStream = Files.newInputStream(file)
        val bytes = inputStream.readAllBytes()
        bytes.toLines().forEach(::println)
        val cursor = IteratorCursor.create(bytes.iterator())
        val target = "beam-file"
        val parser = ParserImpl()
        val parseResult = parser.parse(target, cursor)
        val tree = when(parseResult){
            is Result.Success -> {
                parseResult.tree
            }
            is Result.Failure -> throw RuntimeException(parseResult.message)
        }
        val assembler = AssemblerImpl()
        val assembled = assembler.assemble(target, tree) as BeamFile
        val summary = assembled.toBeamFileSummary()
        println(summary)
        assembled.sections.map{it.name}.forEach(::println)
    }
}
/*
46 4F 52 31 FOR1
00 00 08 E8 2280
42 45 41 4D BEAM
41 74 55 38 AtU8
00 00 01 3E 318

 00 00 00 21 0F 45 6C 69 78 69 72 2E ...>...!.Elixir.
48 65 6C 6C 6F 41 70 70 08 5F 5F 69 6E 66 6F 5F HelloApp.__info_
5F 0A 61 74 74 72 69 62 75 74 65 73 07 63 6F 6D _.attributes.com
70 69 6C 65 0A 64 65 70 72 65 63 61 74 65 64 0B pile.deprecated.
65 78 70 6F 72 74 73 5F 6D 64 35 09 66 75 6E 63 exports_md5.func
74 69 6F 6E 73 06 6D 61 63 72 6F 73 03 6D 64 35 tions.macros.md5
06 6D 6F 64 75 6C 65 06 73 74 72 75 63 74 03 6E .module.struct.n
69 6C 06 65 72 6C 61 6E 67 0F 67 65 74 5F 6D 6F il.erlang.get_mo
64 75 6C 65 5F 69 6E 66 6F 04 6D 61 69 6E 0B 6D dule_info.main.m
69 63 72 6F 73 65 63 6F 6E 64 0D 45 6C 69 78 69 icrosecond.Elixi
72 2E 53 79 73 74 65 6D 0E 6D 6F 6E 6F 74 6F 6E r.System.monoton
69 63 5F 74 69 6D 65 04 61 72 67 76 0B 45 6C 69 ic_time.argv.Eli
78 69 72 2E 4C 69 73 74 05 66 69 72 73 74 0B 45 xir.List.first.E
6C 69 78 69 72 2E 46 69 6C 65 05 72 65 61 64 21 lixir.File.read!
13 45 6C 69 78 69 72 2E 53 74 72 69 6E 67 2E 43 .Elixir.String.C
68 61 72 73 09 74 6F 5F 73 74 72 69 6E 67 06 73 hars.to_string.s
74 72 69 6E 67 06 62 69 6E 61 72 79 03 61 6C 6C tring.binary.all
09 45 6C 69 78 69 72 2E 49 4F 04 70 75 74 73 01 .Elixir.IO.puts.
2D 0B 6D 6F 64 75 6C 65 5F 69 6E 66 6F 14 2D 69 -.module_info.-i
6E 6C 69 6E 65 64 2D 5F 5F 69 6E 66 6F 5F 5F 2F nlined-__info__/
31 2D 00 00 43 6F 64 65 00 00 01 3F 00 00 00 10 1-..Code...?....
00 00 00 00 00 00 00 B1 00 00 00 14 00 00 00 05 ................
01 10 99 00 02 12 22 10 01 20 3B 03 95 17 08 12 ......".. ;.....
32 85 42 85 52 75 62 65 72 55 82 75 92 85 A2 45 2.B.RuberU.u...E
B2 35 01 30 40 C2 03 13 01 40 40 12 03 13 01 50 .5.0@....@@....P
40 47 00 03 13 01 60 40 47 10 03 13 01 70 40 02 @G....`@G....p@.
03 13 01 80 40 03 13 40 12 03 4E 20 00 01 90 06 ....@..@..N ....
10 0D 13 01 A0 99 10 02 12 F2 00 01 B0 0C 20 00 .............. .
AC 17 20 04 14 40 0A 10 03 99 20 07 10 10 40 03 .. ..@.... ...@.
14 99 30 07 00 20 07 10 30 07 10 40 40 03 04 35 ..0.. ..0..@@..5
C5 03 40 04 03 3D D5 01 C0 AC 17 10 04 99 40 07 ..@..=........@.
10 50 01 D0 B1 05 00 10 10 03 17 08 12 0A 1A 00 .P..............
80 02 00 71 0A 1B 20 80 02 03 0A 1C 0A 1A 00 80 ...q.. .........
02 70 11 07 10 60 40 0A 10 03 99 50 07 10 10 99 .p...`@....P....
60 7D 05 10 70 03 14 03 88 20 00 99 70 07 10 50 `}..p.... ..p..P
B1 05 00 10 10 03 17 08 12 0A 1A 00 80 02 80 51 ...............Q
0A 1B 20 80 02 03 0A 1C 0A 1A 00 80 02 D0 D1 08 .. .............
10 60 00 01 E0 99 00 02 12 0A 20 00 01 F0 40 12 .`........ ...@.
03 4E 10 80 01 08 10 99 00 02 12 0A 20 10 01 08 .N.......... ...
11 40 03 13 40 12 03 4E 20 00 01 08 12 99 00 02 .@..@..N .......
12 0A 21 10 01 08 13 3D 0D 12 03 00 53 74 72 54 ..!....=....StrT
00 00 00 1A 48 65 6C 6C 6F 2C 20 21 54 6F 6F 6B ....Hello, !Took
20 20 6D 69 63 72 6F 73 65 63 6F 6E 64 73 00 00   microseconds..
49 6D 70 54 00 00 00 70 00 00 00 09 00 00 00 0D ImpT...p........
00 00 00 0E 00 00 00 02 00 00 00 11 00 00 00 12 ................
00 00 00 01 00 00 00 11 00 00 00 13 00 00 00 00 ................
00 00 00 14 00 00 00 15 00 00 00 01 00 00 00 16 ................
00 00 00 17 00 00 00 01 00 00 00 18 00 00 00 19 ................
00 00 00 01 00 00 00 1D 00 00 00 1E 00 00 00 01 ................
00 00 00 0D 00 00 00 1F 00 00 00 02 00 00 00 0D ................
00 00 00 0E 00 00 00 01 45 78 70 54 00 00 00 34 ........ExpT...4
00 00 00 04 00 00 00 20 00 00 00 01 00 00 00 11 ....... ........
00 00 00 20 00 00 00 00 00 00 00 0F 00 00 00 0F ... ............
00 00 00 00 00 00 00 0B 00 00 00 02 00 00 00 01 ................
00 00 00 02 4C 69 74 54 00 00 00 3C 00 00 00 33 ....LitT...<...3
78 9C 63 60 60 60 62 60 60 10 6C CE 01 92 8C 19 x.c```b``.l.....
4C E5 2C B9 89 99 79 89 0C 59 40 AE 58 73 2E 90 L.,...y..Y@.Xs..
14 98 7B A1 D2 93 81 53 63 F7 F5 2D DE D5 2D 1C ..{....Sc..-..-.
33 1E 03 00 E9 8C 0C E2 4C 6F 63 54 00 00 00 10 3.......LocT....
00 00 00 01 00 00 00 21 00 00 00 01 00 00 00 13 .......!........
41 74 74 72 00 00 00 28 83 6C 00 00 00 01 68 02 Attr...(.l....h.
64 00 03 76 73 6E 6C 00 00 00 01 6E 10 00 D6 51 d..vsnl....n...Q
4C 1B 42 F8 DF 2C E1 A2 98 42 E1 DE 08 F9 6A 6A L.B..,...B....jj
43 49 6E 66 00 00 00 CF 83 6C 00 00 00 03 68 02 CInf.....l....h.
64 00 07 76 65 72 73 69 6F 6E 6B 00 05 38 2E 32 d..versionk..8.2
2E 34 68 02 64 00 07 6F 70 74 69 6F 6E 73 6C 00 .4h.d..optionsl.
00 00 04 64 00 19 6E 6F 5F 73 70 61 77 6E 5F 63 ...d..no_spawn_c
6F 6D 70 69 6C 65 72 5F 70 72 6F 63 65 73 73 64 ompiler_processd
00 09 66 72 6F 6D 5F 63 6F 72 65 64 00 0F 6E 6F ..from_cored..no
5F 63 6F 72 65 5F 70 72 65 70 61 72 65 64 00 0E _core_prepared..
6E 6F 5F 61 75 74 6F 5F 69 6D 70 6F 72 74 6A 68 no_auto_importjh
02 64 00 06 73 6F 75 72 63 65 6B 00 49 2F 55 73 .d..sourcek.I/Us
65 72 73 2F 73 65 61 73 68 75 62 69 2F 67 69 74 ers/seashubi/git
68 75 62 2E 63 6F 6D 2F 53 65 61 6E 53 68 75 62 hub.com/SeanShub
69 6E 2F 65 6C 69 78 69 72 5F 70 72 6F 74 6F 74 in/elixir_protot
79 70 69 6E 67 2F 6C 69 62 2F 68 65 6C 6C 6F 5F yping/lib/hello_
61 70 70 2E 65 78 6A 00 44 62 67 69 00 00 02 D3 app.exj.Dbgi....
83 50 00 00 0A 60 78 9C AD 56 41 6F 9B 30 14 26 .P...`x..VAo.0.&
09 4D 02 6D B7 69 D5 B4 6B 7B DF 82 7A AD BA 4A .M.m.i..k{..z..J
D5 B4 69 95 26 ED D0 ED 6C 19 FC 08 A6 60 23 DB ..i.&...l....`#.
44 CD BF DF 83 40 21 81 B2 34 6B 2E 91 CD F3 E7 D....@!..4k.....
EF 7B 7E DF B3 A3 09 B3 4E 19 F8 F9 92 70 11 4A .{~.....N....p.J
B2 BA 64 96 0B 09 7F E4 8A 80 4A 22 FC EC 54 C3 ..d......J"..T.
D5 A5 B1 2C EB 84 59 27 34 34 80 63 50 3C 5C C7 ...,..Y'44.cP<\.
18 4F 8D 51 DC CF 0D 68 1C 9D 04 32 CD 78 02 44 .O.Q...h...2.x.D
66 A6 18 1F 33 08 B9 E0 86 4B A1 13 04 18 45 76 f...3....K....Ev
34 66 96 9D 52 2E A8 C5 AC 09 7E DF CC 17 B3 09 4f..R.....~.....
17 40 27 71 15 D8 99 8F E3 92 11 21 7E 22 83 07 .@'q.......!~"..
42 CA C0 59 31 37 FA B2 13 6C 97 DF C6 65 BC E1 B..Y17...l...e..
29 90 1C 49 6C A6 30 66 86 EC 35 52 A2 56 7B 05 )..Il.0f..5R.V{.
B2 11 3C 41 CE 29 0F 94 D4 10 48 C1 E2 5E F4 A3 ..<A.)....H..^..
06 FD AC 15 AC 89 0F A1 54 D0 DD 67 6B ED 66 9F ........T..gk.f.
68 52 42 2F FA A1 F1 5C BE 95 89 5F DC AF B5 81 hRB/...\..._....
94 59 6F 52 29 A4 91 82 07 A4 10 14 F7 AE 1B BD .YoR)...........
40 70 4D 24 EE D7 38 6D 34 4E 0D 55 4B E8 41 1B @pM$..8m4N.UK.A.
B7 C3 07 65 4D 6B 59 C7 95 AC EF 58 24 CC 3A 52 ...eMkY....X$.:R
40 D9 45 FC 84 EC 08 49 32 AA 40 68 5C 68 54 0E @.E....I2.@h\hT.
BB 08 A3 7D F1 7F 72 6D 10 3F E4 4A 9B D7 C4 DF ...}.rm.?.J....
3D 16 1B 53 B3 DA 73 83 B8 AC E0 5E FC 59 8D EF =..S..s....^.Y..
54 F8 77 BF F0 53 96 A3 89 7A 23 CB 83 B6 AF AF T.w..S...z#.....
6F 6E 9A AD 69 C2 97 22 05 61 DA A7 BC 09 2F 37 on..i..".a..../7
1D 5F 5D 3D 05 7F 44 BF 83 52 C0 88 CF 8D 46 03 ._]=.D..R....F.
8B 25 D1 19 04 1D D6 15 B1 B4 70 DA 0F 48 12 F9 .%........p..H..
E9 BC 2C 09 1F FD AB D6 1D 6E 9B 62 AA 76 EA EA ..,......n.b.v..
FB 97 FA B3 3A BB 25 A3 C5 D7 88 2A 5D 14 B4 24 ....:.%....*]..$
1B 8E 03 C9 D8 A7 48 67 75 C9 B7 24 6C 53 3E 38 ......Hgu..$lS>8
39 A3 8B E1 B4 3C E3 B2 79 E3 B2 F7 5B 9D A4 EC 9....<..y...[...
B2 5D 31 93 F6 D2 41 C7 CD 0F 6C 24 F3 17 37 92 .]1...A...l$..7.
F9 70 23 71 1A 89 1F B6 24 B2 5C D1 E2 62 E8 82 .p#q....$.\..b..
DB ED D5 83 2A 9D 5A E5 14 B3 45 C5 12 43 3E EF ....*.Z...E..C>.
2A 72 0E CF 71 B3 FB 01 9D DE 69 E5 A5 97 BC BB *r..q.....i.....
B7 E9 DD 97 99 DE FD 4F D3 BB 4D 5D 1F FD 96 F2 .......O..M]....
61 C0 F2 EE B3 96 77 07 2D EF 1E 6A F9 56 2A 0E a.....w.-..j.V*.
28 27 F7 95 3B 40 2B 53 A7 E7 6D 3A C3 19 2B 7F ('..;@+S..m:..+
F8 76 62 90 29 08 A8 01 86 23 3B C4 3B B1 40 BA .vb.)....#;.;.@.
F3 FE 68 A4 EE 69 A0 3A CA 7D EE 2D B9 C1 FF 05 ..h..i.:.}.-....
BE AD BC 7B A0 E2 BE 98 14 5E F5 32 CB 14 BA D9 ...{.....^.2....
AC 33 E4 E8 25 DC F7 A2 A2 4B 13 9A 65 0B 78 C4 .3..%....K..e.x.
07 19 2F 6A 35 A2 2B 2E 73 55 5C 88 34 D1 50 53 ../j5.+.sU\.4.PS
29 7C 93 4A 96 17 57 F1 DB EA 1C CA 26 7F 9B 65 )|.J..W.....&.e
D8 38 B0 BE 31 A3 2B 20 35 B1 77 5D FC 29 66 27 .8..1.+ 5.w].)f'
0F CC D3 DB 29 17 78 A5 07 11 F5 13 88 E3 BF 19 ....).x.........
96 CD 1A 00 44 6F 63 73 00 00 00 68 83 50 00 00 ....Docs...h.P..
00 72 78 9C 4D CC CB 0E 40 30 14 04 D0 F1 AC 8D .rx.M...@0......
FF 60 25 BE 48 6E DC 4A 8B DE 26 D4 E3 F3 A9 95 .`%.Hn.J..&.....
C5 6C E6 64 C6 28 86 62 3F EE C3 D9 53 CA 28 F5 .l.d.(.b?...S.(.
6A 6F BB 39 00 75 D0 77 E8 1C 6D 0B FB 4B 18 B9 jo.9.u.w..m..K..
78 D1 E1 05 AC 6F 12 53 98 8C 51 4D 87 8C C1 FA x....o.S..QM....
E8 8E AC 10 28 FB 38 3E 94 B1 69 DA F9 BF 9D 1F ....(.8>..i.....
FB 69 1D 60 45 78 43 6B 00 00 00 65 83 68 02 64 .i.`ExCk...e.h.d
00 11 65 6C 69 78 69 72 5F 63 68 65 63 6B 65 72 ..elixir_checker
5F 76 31 74 00 00 00 01 64 00 07 65 78 70 6F 72 _v1t....d..expor
74 73 6C 00 00 00 01 68 02 68 02 64 00 04 6D 61 tsl....h.h.d..ma
69 6E 61 00 74 00 00 00 02 64 00 11 64 65 70 72 ina.t....d..depr
65 63 61 74 65 64 5F 72 65 61 73 6F 6E 64 00 03 ecated_reasond..
6E 69 6C 64 00 04 6B 69 6E 64 64 00 03 64 65 66 nild..kindd..def
6A 00 00 00 4C 69 6E 65 00 00 00 2E 00 00 00 00 j...Line........
00 00 00 00 00 00 00 0B 00 00 00 07 00 00 00 01 ................
12 31 51 61 71 81 91 A1 00 10 6C 69 62 2F 68 65 .1Qaq.....lib/he
6C 6C 6F 5F 61 70 70 2E 65 78 00 00 54 79 70 65 llo_app.ex..Type
00 00 00 1A 00 00 00 01 00 00 00 01 1F FF 00 00 ................
00 00 00 00 00 00 FF FF FF FF FF FF FF FF 00 00 ................
 */