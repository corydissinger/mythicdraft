select * from set where name like '%C0%';

SELECT * from draft_pack where set_id in (522,
38463,
70445,
77264,
85981);

delete from draft_pack_available_pick where draft_pack_pick_id in (SELECT dpp.id from draft_pack_pick dpp
									     join draft_pack dp on dp.id = dpp.draft_pack_id
									     where dp.set_id in (522,
38463,
70445,
77264,
85981));

delete from draft_pack_pick where draft_pack_id in (SELECT id from draft_pack where set_id in (522,
38463,
70445,
77264,
85981));

delete from draft_pack where set_id in (522,
38463,
70445,
77264,
85981);

delete from format where first_pack in(
522,
38463,
70445,
77264,
85981
);

delete from set where id in (522,
38463,
70445,
77264,
85981);

select * from format;

delete from format_pick_stats where format_id in (92226, 92229, 92230, 92232);
delete from format where id in (92226, 92229, 92230, 92232);
