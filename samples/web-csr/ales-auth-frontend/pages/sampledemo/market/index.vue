<template>
  <v-container fill-height>
    <v-layout pa-0 ma-0 row wrap>
      
      <v-flex 
        pt-1
        pb-3
        px-2
        md4
        sm6
        xs12
        @click="clickSignin()"
        >
        <v-card elevation="4">
          <v-layout pa-0 ma-0>
            <!-- 画像 -->
            <v-flex pa-2 pr-3 xs5>
              <v-layout ma-0 row wrap fill-height align-center>
                <v-img
                  aspect-ratio="1"
                  cover
                  :width="100"
                  :src="'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAYAAABccqhmAAAAAXNSR0IArs4c6QAAG71JREFUeF7tnQuQXFWZx//f6UeYRxKS8AgziRJFWGRXWfOApVwd1zBjz4sJOLiirlrAlLjIiuCiLGoEdUVKsyL4CMpKEERHnDCTpAVSYVwtArisui4PEUUSCIRXMPPKdPe939bJTtgBA+k73fee293/W5WaBM45/+/8vtP/uX3veQh4kQAJ1CwBqdmes+MkQAKgAXAQkEANE6AB1HDy2XUSoAFwDJBADROgAdRw8tl1EqABcAyQQA0ToAHUcPLZdRKgAXAMkEANE6AB1HDy2XUSoAFwDJBADROgAdRw8tl1EqABcAyQQA0ToAHUcPLZdRKgAXAMkEANE6AB1HDy2XUSoAFwDJBADROgAdRw8tl1EqABcAyQQA0ToAHUcPLZdRKgAXAMkEANE6AB1HDy2XUSoAFwDJBADROgAdRw8tl1EqABcAyQQA0TKNUApKWlZVZjY+NiVV0oIokaZsmuk0BUBAqTk5M78/n89uHh4UkAOlPhQAbQ29ubHh0dPS6RSJzs+/5KETlaVZtFJDnTAFiPBEhgxgQKAB4H8KCIbFDV4fr6+of6+/tzxbZYlAFkMpk5AN5pjPkAgBUAZhUrwHIkQAKREdgjInd7nrculUr1Dw4OjhxI+RUNoLW1tSGdTmdE5ExVfauq1h2oQf5/EiAB5wQmAPwUwLVjY2PZ4eHh0ZeL6GUNYNWqVQtyudzHAJwDYJ7zLjEAEiCBoAR2A7gunU5/bmBg4Kn9Vd6vAbS3ty8EcLkx5lRVbQyqyvIkQAKxIWAfEt7h+/7Z2Wz2sZdG9WcG0NbWdlQymcwCOCo2XWAgJEACJREQkYeNMZnBwcGHpzf0IgPo7u5uKhQKa0SkE0B9SYqsTAIkECcCz4vINSLypaGhoWf2BfaCAfT09BxcKBT6VPXjAA6JU+SMhQRIoHQCImKfA1zqed712WzWPh/AXgPo6+tLbdu27YRkMnm1qh4HgBN6SufNFkggbgQ8APcnEolzFi5ceM/atWvzew0gk8kcmkgkLlFV+57fvvPnRQIkUJ0EdovId1Op1OftmwHp6uqq9zyv0xizRlWbqrPP7BUJkMA0AjuMMecD2CDd3d2He553KYAzAPCVH8cJCVQ/ATsx6IeJROJiewfwJt/3b5p67VfU1ODq58MekkBVE1BV/YPneadLR0fHhfbJv4gcVtVdZudIgASmE3gawGXWAO4SkTdwnj9HBwnUFIEJVR2Uzs7OB1R1CVf41VTy2VkSsEuGN9s7gCcBLADANf0cFCRQOwTsXgJbpb29fUJE0gBM7fSdPSWBmiegIvIrewcw4+2Eah4hAZBAZRO4jwZQ2Qlk9CRQCgEaQCn0WJcEKpxAqAYwDuBuAPcB2KWqfoXDYvgkEBkBETGqOl9E3gzgWAD2OV25r9AMYExVbwZw0aZNm+xbBl4kQAIzINDZ2dmsqleKSCaEuTqhGID9Tf+Q7/tnLV68eO+Swxn0m1VIgAQAtLS0JOvr648XkXUAjinz27pQDMDuSJqdmJg4b8uWLXbPcl4kQAIlEAjxLoAGUEJeWJUEIiFAA4gEM0VIIJ4EaADxzAujIoFICNAAIsFMERKIJwEaQDzzwqhIIBICNIBIMFOEBOJJgAYQz7wwKhKIhAANIBLMFCGBeBKgAcQzL4yKBCIhQAOIBDNFSCCeBGgA8cwLoyKBSAjQACLBTBESiCcBGkA888KoSCASAjSASDBThATiSYAGEM+8MCoSiIQADSASzBQhgXgSoAHEMy+MigQiIUADiAQzRUggngRoAPHMC6MigUgI0AAiwUwREognARpAPPPCqEggEgI0gEgwU4QE4kmABhDPvDAqEoiEAA0gEswUIYF4EqABxDMvjIoEIiFAA4gEM0VIIJ4EaADxzAujIoFICNAAIsFMERKIJwEaQDzz8nJRSW9v75zJyckVnuctUdW5xpgHC4XCb0488cTHVq9eXais7jBa1wRoAK4zUIR+b29vYmRkZHEymXy3qp4O4PUA0tOqegB2isj/+L5/r+/7m0TkF9lsdrKI5lmkhgnQAGKe/JUrV86dNWvWewD0qerrRSRVRMijqjokIjcYY+4YGhoaL6IOi9QgARpATJPe1dVVn8vlTk4mk30i8jZVrZtBqM+p6tdE5KsbN27cNYP6rFLlBGgA8UuwtLa2LkqlUp8A8F4Ac0oMkSZQIsBqrk4DiFF2W1pako2Njcep6mdEpE1V68sUHk2gTCCrrRkaQEwymslkZonIW0TkSgBHAzBlDE1V9VHP8842xvyMDwfLSLbCm6IBxCCBra2tDYlE4jRjzOUAFoYUkn0w+CMAn9y0adOTIWmw2QojQANwnLCenp6Dc7nch0TkQgALQg5nJ4BLAfxk+fLl2zhvIGTaFdA8DcBhkuyTfs/zOo0xa1S1KcJQRkTkB77vX7lixYoHaAQRko+ZFA3AUULs5J7x8fHjVfUGEXldmb/zH7BXqpoXkV+KyOeeffbZzVu3bp04YCUWqDoCNABHKbXgfd+/SkTeAeAgR2FY2Z2qesn4+PhNw8PDow7jKIu0vavyfb8DwKcAHAsgoaoFEdGyCETUiKqKiCQBjBhjvpdMJj87MDDwVLnlaQDlJlpEe11dXYd4nvdxEekDcHARVcIusm+QrQ5jkIUd/PT2Ozs7X+X7/joAJxU5azLK8GakpapPicjqRCLxvcHBwZEZNfIylWgA5aRZRFu9vb3p8fHxtwC4BsCrAUgR1aIoYr8CZCcmJs7bsmXL41EIhqHR1dW1xPf9fgDH29/+YWg4aPMZVf3XXC73nc2bN/+pnPo0gHLSLKIt+9vf9/2LAJwVk9/+06PeLSLrUqnUZZV6J0ADKGIQTitCAwjGq6TSS5cuTR1xxBHLVPXbAP4i6gd/xQQvIvZ287Micn25bzeL0S+1DA0gGEEaQDBeJZVua2ubn0wmPwrgXADzSmosvMp25eDGXC730dtvv31HeDLhtEwDCMaVBhCMVymlpaur60j7gEpVT4j5A6pHAbyvubn5rrVr1+ZL6XTUdWkAwYjTAILxmnFpO903nU6fpqphTvedcXwvqfikiFw0Ojr640p7NUgDCDYEaADBeM24dFtb2xGpVOpyVT0VQMOMG4qmYsWuHqQBBBsgNIBgvGZaWtrb218nIpsAvHamjURYjwYQIewipPgacApSRb6rttN+9+zZ80bP824WkSOLSLjrIhVrAN3d3U2e530ZQDeAcu2n4DofO4wx5wPYUO7t3XgHEEFq+/r6Utu3bz/JGGNnqL0qAslSJSrWAKzZTk5OLvE87zwAfwdgO4CK3BxVRBK+79s5wesTiUR/GK9laQClflSKqN/S0nJQQ0PD2wF8E8CiIqq4LlKxBuAaXKXp0wAiyNi+6b8icq2qLo5AsiQJOxnI7hvged712Wx2d0mNsXKsCdAAokmPZDKZY40xQwBeE41kSSqPG2POHRkZ+cnw8PCeklpi5VgToAFElJ4KW6X2qIi8e9myZb/gZiERDRBHMjSAiMDbeQDJZPIKAD0xnwfgA/iNMWbV0NDQIxHhoYwjAjSAiMDHfBXgdAqTIrLF87y+bDb7WER4KOOIAA0gIvDd3d2zfd//e1W9DMDhEckGllFVu978O6r6xWw2+3TgBlihogjQACJKVwXNBXgWwBWpVOpb69evfz4iPJRxRIAGEBH41tbWw5LJpD3x530AZkckOxMZu3feIyJyjud5P+UhIjNBWDl1aAAR5aq9vf3VdpMNVT0x5kuBLZEnVfWT4+PjP6q01YARpbNqZGgAEaTSnvk3d+7cpYVC4SYAlbAW4Gnf97+Qz+f/vdx70EWAmxIBCNAAAsCaaVE7FXj27Nkrfd+3U4GbZ9pOhPXsc4CvAPgGjxWPkLoDKRpABNDtHcDs2bOX+b7//Qq5A7BHiH26vr7+xv7+/oo/KyCCFFesBA0gotS1tbUdlUwmswCOikiyFBm7Lfh5zz33XJYnBpWCMf51aQAR5WhqnfoaEelU1TivU7dHiT9k19Nv2rTpdwAq6kSdiNJZNTI0gIhS2dHRMU9V/xHA+SIyPyLZmcjYXYFvKRQKF9x6661PzKQB1qkcAjSAiHLl8CTgoD3kXgBBiVVweRpAhMmzXwPskdwA2lW1LkLpYqXsQqDf2lOLmpubf1FpW4IX20mW+38CNIAIR4NdD1AoFM6wm22IyGERShclxXUARWGqqkI0gIjTaVcFquonVNWeDTg3YvlXkhsXkazv+19ctGjRr/nbP0aZCTEUGkCIcF+uabswaMeOHUeq6tEiYu8IulW10UEo9pb/EVW92Pf9X8+ePfuR/v7+nIM4KOmIAA3AEXgr29PTc3ChUDhHVS8AsMBBKBMi8hMAH9mwYUPFHgnugFvVSNIAHKayt7e3bnx8PCMiV6pq5FOE7Xd+Efm2MeaLQ0NDzzhEUVZpy3ViYuJ4z/MOERFT1sYjbkxVJzzP+9VJJ530zOrVq+0dW1kvGkBZcQZuTDo6OpYAuBHAUgDJwC2UUEFVnzLGfMYYc0MYe86XENqMq9oTmFOp1Hmqas8FiOsJzIH6p6p/9DzvXXPmzLm3v7/fC1T5AIVpAOWkOYO2MpnMocaYTwF4P4A5M2hiplXsb5P7ReTMZcuW/Ve1bP7JswGDDQcaQDBeZS+dyWRmicjbROSaKA8NqdbbfxpAsCFKAwjGK5TSDu4C7G3kA57nfVREfl5Nu/7QAIINURpAMF5hlTYrV648Mp1Of8kYE8UswZ32u7+I3Fgt3/33JYYGEGyI0gCC8Qqt9NKlS1NNTU32yfUVIvI3ANIhidntvi4eHx/vr8btvmgAwUYNDSAYr1BLh5iM6XE/JiIfHh0dvb0aj/2iAQQboiGOufvsK65yryWfAJCdmJg4b8uWLVU3cSXEZLwwKkRku6qe7fv+cDV99+dXgGAf/H2lQxxzNICgKQkxGdND2eb7/gcbGxt/Xo3TfnkHEGzUhTjmaADBUgGEmIzpodiDP9/b1NR0dzUu+KEBBBt1IY45GkCwVACZTGaRiHxdRE4GcFDQ+kWW/6OIvKuurq7ss8qK1A+12KpVqxZMTk5eICIfqpaZgAAeBXDG8uXL7yn3hC0aQKjDMVjjp5xyymLP876tqm8FMCtY7aJLP+L7/mknnHDCr8OYW150FCEWbG1tbTDGnGiMObwa1gIA+M+GhobHyj0N2KaABhDiQAzadGdn56tU9ToAJ4X4GvAPxpjupUuXPlCtBhCUey2XpwHEKPtTx4fdqKorRCSshUG/F5HMhg0b7I6/vGqcAA0gRgOgp6fnyHw+3w/grwEkwghNRB42xmQGBwcfDqN9tllZBGgAMcpXR0fHa0RkQFX/EkAo69hpADFKeAxCoQHEIAn7QshkMq81xmwAcAwACSM0GkAYVCu3TRpAPHJnZ00eDGAVgM8DWBhiWM/ZQ0qNMd9vbm7+bTXOBQiRXdU1TQNwmNLe3t5EPp+33/vtO2u7IcghYf3m30837aGftwD4lu/791TjtGCHqa0YaRqAm1TJqlWrDs3lcvaD/0EArwrrO38R3dutqvbU4m8uWrToPt4RFEGsiorQACJOpp2kkkql2gF8GIBd9hvWhJ8gPbMbhPxeVa/O5XLXbd68+U9BKrNs5RKgAUSUu5aWluSsWbOOSSaT9oDQ90S8/1+xvbSHg9zm+/7Xx8fHf1aNy4WLBVEr5WgAEWQ6k8nMMca8d+p0YHsYSFiTfMrVm12qah8UXr1s2bInOGOwXFjj1w4NIMSctLS0HNTY2HiiqtrbfXvb3xCiXLmbnrRz0FXV3g0MVuPuQeUGVont0QBCypo9A9DzPHu7/2EROTTCp/vl7tFuAD8Qkaubmpru50PCcuN12x4NoMz8u7q66lX17ar6EQB/G+Ky3jJH/orNFQA8JCJf8zzvxmw2a02BVxUQoAGUKYmrV682d955Z3M6nT7XbrlVRWvRpxOycwfWq+pVDQ0Nv6zGHYXKNBwqphkaQBlSZV/tpdPpTlU9F8DymLzaK0PP9tuEJyJ2W7Gr0un0tevXr38+LCG2Gz4BGkAJjO1MvomJidcAsL/17Uy+uSU0V2lVxwHYk4Wv8n3/Ts4krLT0/V+8NIAZ5q27u3t2oVA4zRhjP/x/FeIGHjOMMJJqdtfnJ60JAPjGxo0bd0WiSpGyEaABBERpv+vfc889i0Xkk3afNgCzAzZRjcXtdu1bjDFXFAqFu3g3UDkppgEEyFVfX1/qiSeeON73/Uvsxp2qWhegerUXtW8KHjTGXDY5ObnxtttuG6v2DldD/2gARWaxt7e3bs+ePW/3ff8LAF4f1o49RYYT22Ii8hSAyzzPW8fXhbFN0wuB0QCKyFFvb2/j+Pj46QDsh//wIqrUepHnAPybfTbA5wLxHgo0gAPkp6OjY97UHP7zRWR+vNMZq+jGROSWfD5/4a233vpEhJGZ1tbW5mQyeSaAJRW+LXhCVSEig2NjYxvCmI5NA3iFkdnW1nZEMpn8MoBTANRHOIirRcoHcJ+q/sOKFSv+O4pFRTZnqVTqclU9tcLWXrxSzh9X1X/atWvXpq1bt9oHrmW7aAAvgzKKDTrLlsX4N7RDVc/fuXPnhnvvvdfOHwjt4tFgwdDSAPbDK6ITeoJlqvJL7wTw6fr6+hv7+/vtlOJQLhpAMKw0gJfwsqv4fN+/CMBZAOxGnbzKQ8AuL77D9/2zs9nsY+Vp8s9boQEEI0sDeAmviI7nCpal6im9TUTeX1dXd2dYC4loAMEGCw1gGi87t3/Pnj1v9DzvZhE5MhhKli6CwBPGmIsmJyd/HNZEIRpAEVmYVoQG8GIDqBsZGXlHIpH4GoDmYChZuggCzwL4SpjrBmgARWSBBrB/SFN7971fVe1U38OCoWTpIgiMiMgNqVTqMwMDA3bGYNkvGkAwpLwDmMbLPgBU1X+e2tCDDwCDjaViStsHgcP5fP7s2267bXsxFYKWoQEEI0YDmMaru7u7yfO8r4pIBxf6BBtIAUr/LpFIdA0ODj4EwC4nLus1dcT691T1BBFJlbVxR42Fub6CBvDiO4Alvu/b47mP52KfcEa7qm4vFApnJhKJ/whj2XBLS0tjfX39KhFZDcA+yA3llOVw6Oy3VTtn4oeJROLiwcFBO5eirBcNYAqnPbhj7ty5SwuFwk1TA6esoNnYCwTsIP6XsbGxH4Qxt30aZ+nt7U2Njo6GcspyVPlsbGzU/v7+fBh3S7YPNICpTNrdfD3P6zTGrFHVpqgSXIM6dtegr6fT6TUDAwP2rQAvhwRoAFPwe3p6DraHdYrIhQAWOMxJtUvbtQC3FAqFCyJeJVjtXGfUPxrAtDuAQqHQaoyxp/UuATDfLv9V1YMq+FCPGQ2KECvZXYMeUdV19qAR7hUQIukim6YBvDIoaW1tra+rq5ufz+cXqOp8Y8zenyJi7xLs/gD7ftrXhvboL7ts2P7Z93drIJX0IMo+mbenBeem/kyqak5E7L/ta7x9/30PALs01R4SYv+M7Pu7iOz9b57n7f2579/25/Lly0dWr15t2y/7G4AixzyLTSNAA4h2OJhMJpOamJhINTQ0pNLpdCqfz6dEJO15XiqRSKT2/SwUCulkMplU1b0PsexP+1pLROzJQw2+7zdakxERuy+h/TDl7AfVGPPCB9X+2/6xH1z7M5FI2A/y3r/v++++7+c8z8v5vj85b968vR/u/v5+u46fH9Box4YTNRqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKABxCMPjIIEnBCgATjBTlESiAcBGkA88sAoSMAJARqAE+wUJYF4EKg0A7Cn1j7k+/5Zixcvvmft2rX5eGBkFCRQeQRaWlqS9fX1x4vIOgDHlPkY+/uko6MjjCOmx1T1ZgAXbdq06cnKw86ISSAeBEL87W87GJoB2MbHAdxtRQDsUlV7Z8CLBEigCAIiYlR1voi8GcCxANJFVAtaJFQDCBoMy5MACURLgAYQLW+qkUCsCNAAYpUOBkMC0RK4T9rb2ydExH6/MNFqU40ESMAhARWRX9m3APYp/QIASYfBUJoESCBaAgUAW6Wzs/MBVV0CYFa0+lQjARJwSCAHYLO9A7hLRN6gqnUOg6E0CZBAtAQmVHXQGsCFqvpxETksWn2qkQAJOCTwNIDLpKur602+798E4CgA4jAgSpMACURDQFX1D57nnS7d3d2He553KYAzADRGo08VEiABhwRGAfwwkUhcbO8A6j3P6zTGrFHVJodBUZoESCAaAjuMMecD2LD3lj+TyRyaSCQuUdUPAJgTTQxUIQEScEBgt4h8N5VKfX5gYOCpvQbQ19eX2rZt2wnJZPJqVT0OQMJBYJQkARIIl4AH4P5EInHOwoUL9y7Vf+GhX09Pz8GFQqHPvhEAcEi4cbB1EiCBqAmIyFMALvU87/psNrvb6r/oqX93d3dToVBYIyKdAOqjDpB6JEACoRF4XkSuEZEvDQ0NPbNP5c9e+7W1tR2VTCazU68FQ4uGDZMACURHQEQeNsZkBgcHH56uut/3/u3t7QsBXG6MOVVV+WowujxRiQTKTWASwB2+75+dzWYfe2njLzvxZ9WqVQtyudzHAJwDYF65o2J7JEACoROw3/OvS6fTn7NP/Pen9ooz/1pbWxvS6XRGRM5U1bdyvUDoCaMACZSDwASAnwK4dmxsLDs8PGwn/uz3KmrqbyaTsXMD3mmMsfMEVnDlYDlyxDZIoOwE9ojI3Z7nrUulUv2Dg4MjB1IoygD2NdLb25seHR09LpFInOz7/koROVpVm0WEewkciDT/PwmUn4Bd0/84gAdFZIOqDtfX1z/U399vl/oWdQUygP20KC0tLbMaGxsXq+pCEeEEoqKwsxAJlESgMDk5uTOfz28fHh62D/lmvLV/qQZQUi9YmQRIwC0BGoBb/lQnAacEaABO8VOcBNwSoAG45U91EnBKgAbgFD/FScAtARqAW/5UJwGnBGgATvFTnATcEqABuOVPdRJwSoAG4BQ/xUnALQEagFv+VCcBpwRoAE7xU5wE3BKgAbjlT3UScEqABuAUP8VJwC0BGoBb/lQnAacEaABO8VOcBNwSoAG45U91EnBKgAbgFD/FScAtARqAW/5UJwGnBGgATvFTnATcEqABuOVPdRJwSoAG4BQ/xUnALQEagFv+VCcBpwRoAE7xU5wE3BKgAbjlT3UScEqABuAUP8VJwC2B/wULDDB01GP8IQAAAABJRU5ErkJggg=='"
                ></v-img>
              </v-layout>
            </v-flex>
            <!-- 情報 -->
            <v-flex xs7 pa-0 pt-2 >
              <v-layout column align-content-end fill-height ma-0>
                <v-card-text style="flex-grow: 1" class="pl-0 pr-2 py-0" max-width="180px">
                  <div>
                    <div class="login-service-name">
                      AzureADB2C
                    </div>
                    <div class="login-title">
                      ログイン
                    </div>
                  </div>
                </v-card-text>
              </v-layout>
            </v-flex>
          </v-layout>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script lang="ts">
import { defineComponent, ref, useRouter } from '@nuxtjs/composition-api'
import { useUserStore } from '~/store/user'
import authPopup from '~/auth/authADB2C'

export default defineComponent({
  name: 'Market',
  setup() {
    const copyright = 'Copyright © 2022 BIPROGY Inc. All rights reserved.'
    const router = useRouter()

    const hamburgerActive = ref<boolean>(false)
    const hamburgerClick = () => {
      hamburgerActive.value = !hamburgerActive.value
    }

    // ログイン実行
    const clickSignin = async () => {
    try {
      await authPopup.signIn()
      router.push('/sampledemo/communities')
      hamburgerActive.value = false
      }catch(err) {
        console.log(err)
      }
    }

    return {
      copyright,
      hamburgerClick,
      hamburgerActive,
      clickSignin,
    }
  },
})
</script>
<style lang="scss" scoped>
.login-service-name {
  padding-top: 8px !important;
  padding-bottom: 8px !important;
  font-size: 20px !important;
  font-weight: 500 !important;
}
.login-title {
  padding-top: 8px !important;
  padding-bottom: 36px !important;

  font-size: 24px !important;
  font-weight: 700 !important;
}
.form-dt {
  padding: 0px !important;
  font-size: 18px !important;
}

</style>