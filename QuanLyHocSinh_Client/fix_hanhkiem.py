#!/usr/bin/env python3
import sys

file_path = r'D:\Documents\Nam 3 Ki 2\CongNghePhanMem\QuanLyHocSinh\src\main\java\View\Tien\HanhKiemPanel.java'

with open(file_path, 'r', encoding='utf-8') as f:
    lines = f.readlines()

# Xóa dòng 198-208 (0-indexed: 197-207)
new_lines = lines[:197] + lines[208:]

with open(file_path, 'w', encoding='utf-8') as f:
    f.writelines(new_lines)

print("Fixed: Removed duplicate fillFormInput method (lines 198-208)")
