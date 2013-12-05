# @file:    intfc_post_tagger.py
# @author:    shicq@cs.brandeis.edu
# @brief:   test post tagger using naive bayes

import sys


code_suffix_pos_tag = input('code_suffix_pos_tag.py')
print code_suffix_pos_tag
pm = __import__(code_suffix_pos_tag)
print pm