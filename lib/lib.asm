





default rel

global __lib_getString
global __lib_toString
global __lib__string_add

extern memcpy
extern strlen
extern __sprintf_chk
extern scanf
extern malloc
extern strncpy


SECTION .text

__lib_getString:
        push    rbx
        mov     edi, 256
        call    malloc
        lea     rdi, [rel LC0]
        mov     rbx, rax
        mov     rsi, rax
        xor     eax, eax
        call    scanf
        mov     rax, rbx
        pop     rbx
        ret


ALIGN   16

__lib_toString:
        push    rbp
        push    rbx
        mov     rbp, rdi
        mov     edi, 50
        sub     rsp, 8
        call    malloc
        lea     rcx, [rel LC1]
        mov     rbx, rax
        mov     r8, rbp
        mov     rdi, rax
        mov     edx, 50
        mov     esi, 1
        xor     eax, eax
        call    __sprintf_chk
        add     rsp, 8
        mov     rax, rbx
        pop     rbx
        pop     rbp
        ret






ALIGN   8

__lib_string_add:
        push    r14
        push    r13
        mov     r13, rsi
        push    r12
        push    rbp
        mov     r14, rdi
        push    rbx
        call    strlen
        mov     rdi, r13
        mov     rbx, rax
        call    strlen
        lea     rdi, [rbx+rax+1H]
        mov     rbp, rax
        call    malloc
        mov     rdx, rbx
        mov     r12, rax
        mov     rsi, r14
        mov     rdi, rax
        call    memcpy
        lea     rdi, [r12+rbx]
        lea     rdx, [rbp+1H]
        mov     rsi, r13
        call    memcpy
        pop     rbx
        mov     rax, r12
        pop     rbp
        pop     r12
        pop     r13
        pop     r14
        ret

ALIGN   16

__lib_str_substring:
        push    r12
        push    rbp
        mov     r12, rdi
        push    rbx
        mov     rbx, rsi
        mov     rbp, rdx
        sub     rbx, rdi
        lea     rdi, [rbx+2H]
        call    malloc
        lea     rsi, [rbp+r12]
        lea     rdx, [rbx+1H]
        mov     rdi, rax
        call    strncpy
        mov     byte [rax+rbx+1H], 0
        pop     rbx
        pop     rbp
        pop     r12
        ret

SECTION .rodata

LC0:
        db 25H, 73H, 00H

LC1:
        db 25H, 6CH, 64H, 00H


